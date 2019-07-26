"""The module analysers provides the StreamAnalyser framework for analysis of
output and error stream of submissions.

StreamAnalysers are module building blocks to be assembled into complex
analysers.
"""
import re
import decimal

from .exc import StudentFailure
from .exc import StudentWarning
from .exc import ConfigurationError
from .severity import Severity
from .severity import Statistics


def compactified_whitespace(string):
    """Compactifies consecutive whitespace to a single space.

    Returns a copy of the string.
    """
    import re
    whitespace_re = re.compile("\s+")
    return whitespace_re.sub(" ", string)


def without_whitespace(string):
    """Removes all whitespace.

    Returns a copy of the string.
    """
    import re
    whitespace_re = re.compile("\s+")
    return whitespace_re.sub("", string)


def normalized(string):
    """Normalizes the output for more tolerant comparison.

    Returns a copy of the string with leading and trailing whitespace stripped,
    everything converted to lower case and internal whitespace compactified to
    a single space.

    This is a sensible default normalization method for most use cases.
    """
    return compactified_whitespace(string.lower().strip())


def _ensure_callable(condition):
    # turn a regular expression into a callable object. This makes it possible to pass
    # either regular expressions or functions (e.g. lambda) functions into an
    # analyser to match lines for certain properties.
    # This is a module private helper function for analysers.
    if hasattr(condition, '__call__'):
        return condition
    else:
        # assume condition is a regular expression and compile it
        import re
        try:
            regexp = re.compile(condition)
        except:
            raise TypeError("Condition needs to be either callable (str->bool) or a string")

        return lambda l: bool(regexp.match(l))


class Annotation(object):
    """A single annotation with a message and a severity.

    This is a pair of a message string and a severity.
    """
    def __init__(self, message, severity=Severity.FAILURE):
        """Create a new Annotation object with a message and a severity. The
        default severity is Severity.Failure."""
        if isinstance(message, str):
            self.message = message
        else:
            self.message = "\n".join(message)
        self._severity = severity

    def __str__(self):
        return self.message

    @property
    def severity(self):
        """Return this Annotation's severity."""
        return Severity.string(self._severity)


class Annotateable(object):
    """An annotateable something.

    This is used to annotate the beginning and end of a stream as well as each
    line. A line in a stream has to Annotateables, one representing annotations
    before the line, and one for annotations to be displayed after the line.
    """

    def __init__(self):
        """Create a new Annotateable.

        No annotations are added."""
        self.annotations = []

    def annotate(self, annotation, severity=Severity.FAILURE):
        """ Add an annotation to the object"""
        if not annotation:
            # do nothing for None
            pass
        elif isinstance(annotation, Annotation):
            # add an existing annotation (ignore severity in this case)
            self.annotations.append(annotation)
        else:
            # add a new annotation
            self.annotations.append(Annotation(annotation, severity))

    @property
    def successes(self):
        """The number of warning annotations attached"""
        return sum(1 for annotation in self.annotations if annotation._severity == Severity.SUCCESS)

    @property
    def infos(self):
        """The number of warning annotations attached"""
        return sum(1 for annotation in self.annotations if annotation._severity == Severity.INFO)

    @property
    def warnings(self):
        """The number of warning annotations attached"""
        return sum(1 for annotation in self.annotations if annotation._severity == Severity.WARNING)

    @property
    def failures(self):
        """The number of failure annotations attached"""
        return sum(1 for annotation in self.annotations if annotation._severity == Severity.FAILURE)

    @property
    def crashes(self):
        """The number of warning annotations attached"""
        return sum(1 for annotation in self.annotations if annotation._severity == Severity.CRASH)


class AnnotateableLine(object):
    """A line from a stream that can be annotated"""

    def __init__(self, content, number = ""):
        self._content = content
        self.number = number
        self.before = Annotateable()
        self.after = Annotateable()
        self.skip = False
        self.hide = False

    def __str__(self):
        if hasattr(self, 'substitute'):
            return self.substitute
        elif isinstance(self._content, str):
            return self._content
        else:
            return self._content.decode()

    def __len__(self):
        return len(str(self))

    def annotate_before(self, annotation, severity=Severity.FAILURE):
        """Add an annotation before this line."""
        self.before.annotate(annotation, severity)

    def annotate_after(self, annotation, severity=Severity.FAILURE):
        """Add an annotation after the line."""
        self.after.annotate(annotation, severity)

    @property
    def successes(self):
        return self.before.successes + self.after.successes

    @property
    def infos(self):
        return self.before.infos + self.after.infos

    @property
    def warnings(self):
        return self.before.warnings + self.after.warnings

    @property
    def failures(self):
        return self.before.failures + self.after.failures

    @property
    def misconfigs(self):
        return self.before.misconfigs + self.after.misconfigs

    @property
    def crashes(self):
        return self.before.crashes + self.after.crashes


class StreamAnalyser(object):
    """Base class for all stream analysers."""
    def __init__(self):
        pass

    def start_stream(self, stream):
        """Callback to be executed when a stream is started."""
        pass

    def analyse_line(self, line):
        """Callback to be execute on a line of a string.

        The parameter line is an AnnotateableLine."""
        pass

    def finish_stream(self, stream):
        """Callback to be executed when a stream is finished.

        Does nothing by default.
        """
        pass


class NullAnalyser(StreamAnalyser):
    """Does nothing at all.

    This class implements the StreamAnalyser interface, but doesn't do anything
    when used. This is useful where a StreamAnalyser is required, but no
    annotations are supposed to be created.
    """
    pass


class NoLineExpectedAnalyser(StreamAnalyser):
    """Prints an error message as soon as any line at all occurs."""

    def analyse_line(self, line):
        if not hasattr(self, 'printed'):
            line.annotate_before("Expected end of output")
            self.printed = True


class ErrorAnalyser(StreamAnalyser):
    """Emits a message if no error message got printed.

    If the whole stream doesn't containa single line starting with 'error',
    print a failure message.
    """

    def analyse_line(self, line):
        # TODO: make this more flexible by using a normalizer.
        if str(line).lower().strip().startswith("error"):
            self.error = True

    def finish_stream(self, stream):
        if not hasattr(self, 'error'):
            stream.annotate("Expected an error message starting with Error,")


class ExceptionAnalyser(StreamAnalyser):
    """Tries to identify uncaught exceptions in the output and filters them out.

    The ExceptionAnalyser implements the decorator design pattern. It tries to
    identify JVM's exception output.  If a block of lines looks like an
    Exception (with a stack trace), these lines are not passed to the decorated
    StreamAnalyser. Furthermore, a failure message is printed after the
    exception message complaining about an uncaught exception.
    """

    def __init__(self, analyser = None):
        self.analyser = analyser
        self.exception = False

    def start_stream(self, stream):
        if self.analyser:
            self.analyser.start_stream(stream)
    
    exception_re = re.compile('^\s*(?:at|caused\sby|\.{3})', re.I)
    def analyse_line(self, line):
        if str(line).startswith("Exception in"):
            self.exception = True
        elif self.exception and not ExceptionAnalyser.exception_re.search(str(line)):
            line.annotate_before("An uncaught exception occured")
            self.exception = False

        if not self.exception and self.analyser:
            self.analyser.analyse_line(line)

    def finish_stream(self, stream):
        if self.exception:
            stream.annotate("An uncaught exception occured")
        elif self.analyser:
            self.analyser.finish_stream(stream)


class MissingMainClassAnalyser(StreamAnalyser):
    """Emits an error message if the main class could not be found.

    For varying reasons, the JVM might not find the requested main method.
    This StreamAnalyser identifies this case and tries to display an
    helpful message. Most of the time this happens because Praktomat identified
    the main method in the .java file, but the java compiler failed to compile
    the class so the .class file is missing.

    This StreamAnalyser also implements the decorator design pattern. If the
    main method could not be found, the decorated analysers will not be
    executed.
    """
    def __init__(self, analyser = None):
        # TODO: possibly use a SkipTheRest with hide=True instead.
        self.analyser = analyser
        self.missing = False

    def start_stream(self, stream):
        if self.analyser:
            self.analyser.start_stream(stream)

    def analyse_line(self, line):
        if (str(line).startswith("Error: Could not find or load main class") or
            str(line).startswith("Error: Main method not found in class")):
            self.missing = True
            line.annotate_after("Check the compiler output, your packages, class names, and main method.")

        if not self.missing and self.analyser:
            self.analyser.analyse_line(line)

    def finish_stream(self, stream):
        if not self.missing and self.analyser:
            self.analyser.finish_stream(stream)


class FinalAnnotationAnalyser(StreamAnalyser):
    """Unconditionally prints out an info annotation at the end of the stream."""
    def __init__(self, message, severity=Severity.INFO):
        """Create a new FinalAnnotationAnalyser.

        message is the message to be displayed. severity is the message's
        severity and Severity.INFO by default.
        """
        self.message = message
        self.severity = severity

    def finish_stream(self, stream):
        stream.annotate(self.message, self.severity)


class LineByLineAnalyser(StreamAnalyser):
    """Compares the output to the list of expected output.

    This StreamAnalyser tries to match the output line-by-line with the
    expected output.  Order of the lines is important for this StreamAnalyser.

    The analyser emits a failure message if the stream doesn't equal the
    expected string."""
    def __init__(self, expected, normalizer=normalized):
        self.expected = iter(expected.splitlines() if isinstance(expected, str) else expected)
        self.normalizer = normalizer

    def analyse_line(self, line):
        # increment line number for the next round
        try:
            expected = next(self.expected)
        except StopIteration as e:
            line.annotate_before("Expected end of output")
            return

        string = self.normalizer(str(line))
        expected = self.normalizer(expected)
        if string != expected:
            line.annotate_after("Expected: " + expected)

    def finish_stream(self, stream):
        try:
            line = next(self.expected)
        except StopIteration as e:
            return
        else:
            hint = line #if len(line) <= 50 else line[:50] + "..."
            stream.annotate("Expected: " + hint)


class LineSetAnalyser(StreamAnalyser):
    """Tries to match any line from a set of lines in any order.

    This StreamAnalyser tries to match the output lines against a set of lines
    in any order. It also tries to be smart about suggesting good matches for
    an input line.  Note, that StreamAnalysers don't peak ahead, so this
    analyser doesn't know if future output lines might be better matches for an
    expected line than the current line.
    A LineSetAnalyser also keeps track of already encountered lines and will
    complain about missing lines when the stream finishes.
    """

    # TODO: don't just store if we found the line, but also if it was an exact
    # match or a close match.  This should make 'Duplicate line' messages more
    # clear to the students.
    def __init__(self, expected, normalizer = normalized):
        """Create a new LineSetAnalyser.

        - expected
          A list of lines expected to be in the output

        - normalizer
          A normalization function to pre-process expected lines and output
          lines for increased tolerance.
        """
        expected = iter(expected.splitlines() if isinstance(expected, str) else expected)
        self.expected = {key:False for key in expected}
        self.normalizer = normalizer

    def close_match(self, line):
        # find the closest match if there is unique closest match
        match = None
        for exp,found in list(self.expected.items()):
            norm_exp = self.normalizer(exp)
            if not found:
                from difflib import SequenceMatcher
                if SequenceMatcher(a=line,b=norm_exp).ratio() > 0.6:
                    if match:
                        return None
                    else:
                        return exp

    def analyse_line(self, line):
        norm_line = self.normalizer(str(line))
        for expected, found in list(self.expected.items()):
            norm_exp = self.normalizer(expected)
            if norm_line == norm_exp:
                if found:
                    line.annotate_after("Duplicate line")
                else:
                    self.expected[expected] = True
                return
        else:
            close = self.close_match(norm_line)
            if close:
                line.annotate_after("Line not expected, did you mean " + close)
                self.expected[close] = True
            else:
                line.annotate_after("Line not expected")

    def finish_stream(self, stream):
        message = []
        for line,found in list(self.expected.items()):
            if not found:
                hint = line #if len(line) <= 50 else line[:50] + "..."
                message.append(hint)
        if len(message):
            stream.annotate("Expected:\n" + '\n'.join(message))


class MatchLineAnalyser(StreamAnalyser):
    """Tries to match each line against a regular expression or matching function."""
    def __init__(self, condition, must_match = True, message="Unexpected line"):
        """Create a new MatchLineAnalyser.

        condition is either function with signature string -> bool indicating
        whether the string is as expected or a regular expression describing
        the expected strings. If must_match is False, the interpretation of
        expected lines is inverted.
        """
        self.condition = _ensure_callable(condition)
        self.must_match = must_match
        self.message = message

    def analyse_line(self, line):
        if self.condition(str(line)) != self.must_match:
            line.annotate_after(self.message)


class NumberOfLinesAnalyser(StreamAnalyser):
    """Counts the number of lines and checks if there are exactly or at least 
    a number of output lines."""
    def __init__(self, number, exact=True):
        """Create a new NumberOfLinesAnalyser.

        number is the expected number of lines
        if exact is false, the analyser checks only if there are at least number lines
        """
        self.number = number
        self.exact = exact
        self.count = 0

    def analyse_line(self, line):
        self.count += 1

    def finish_stream(self, stream):
        if self.exact and self.count != self.number:
            stream.annotate("Expected " + str(self.number) + ", but got " + str(self.count) + " lines.")
        elif not self.exact and self.count < self.number:
            stream.annotate("Expected at least " + str(self.number) + ", but got " + str(self.count) + " lines.")


class FloatingPointAnalyser(StreamAnalyser):
    """Compares each line with a given floating point number at a set tolerance"""
    def __init__(self, expected, digits=9):
        """Create a new FloatingPointAnalyser.

        expected is a list of expected numbers as line break seperated string or float
        digits is the number of decimal places that have to be correct. Because of variance in float values,
        the last digit in the actual value can differ a bit and still be valid
        """
        self.expected = iter(expected.splitlines() if isinstance(expected, str) else expected)
        if digits < 1:
            raise ValueError("Number of decimal places must be greater than zero.")
        self.digits = digits

    def analyse_line(self, line):
        # increment line number for the next round
        try:
            expected = normalized(next(self.expected))
        except StopIteration as e:
            line.annotate_before("Expected end of output")
            return

        expected_f = float(expected)
        actual_f = float(str(line))
        if abs(expected_f - actual_f) > (float(1) / pow(10, self.digits)) * max(abs(expected_f), abs(actual_f)):
            line.annotate_after("Too inaccurate, expected at least " + expected + " or more precise.")

    def finish_stream(self, stream):
        try:
            line = normalized(next(self.expected))
        except StopIteration as e:
            return
        else:
            hint = line #if len(line) <= 50 else line[:50] + "..."
            stream.annotate("Missing line, expected at least " + hint + " or more precise.")


class BlockwiseAnalyser(StreamAnalyser):
    """Run a list of analysers on the stream, switching analysers each time a
    line matches the separator.

    If there are StreamAnalysers left unused in the list the BlockwiseAnalyser
    will treat this as a failure. The same happens if there are blocks left,
    but no StreamAnalyser is left in the list of StreamAnalysers to analyse it.
    """
    def __init__(self, separator, analysers):
        """Create a new BlockwiseAnalyser.

        separator is a matching function or regular expression indicating if a
        line should be treated as a separator.  If a separator is encountered,
        the next analyser from the list (or generator) of analysers is switched
        to.  When switching analysers, the last analyser gets a finish_stream
        signal, and the next one a start_stream signal.  Thus the stream is
        essentially cut into a sequential list of streams.

        This analyser also provides to additional methods to be used when
        inheriting from it.  pre_start_stream() is called before the first
        substream, post_start_stream() is called after the last substream.
        """
        # the separator may either be a callable (e.g. a lambda) or a regular
        # expression. If it is the later create a callable from it
        self.separator = _ensure_callable(separator)
        self.quiet = False

        try:
            self.iterator = iter(analysers)
        except TypeError:
            self.iterator = iter([analysers])
        try:
            self.analyser = next(self.iterator)
        except StopIteration:
            if not self.quiet:
                stream.annotate("Expected no output")
            self.quiet = True
            self.analyser = NullAnalyser()

    def pre_start_stream(self, stream):
        pass

    def start_stream(self, stream):
        self.pre_start_stream(stream)
        self.analyser.start_stream(stream)

    def analyse_line(self, line):
        if not self.separator(str(line)):
            # regular line: pass it to current analyser
            self.analyser.analyse_line(line)
        else:
            # separator line: switch analysers
            self.analyser.finish_stream(line.before)
            try:
                self.analyser = next(self.iterator)
            except StopIteration:
                self.analyser = NullAnalyser()
            self.analyser.start_stream(line.after)

    def finish_stream(self, stream):
        self.analyser.finish_stream(stream)
        try:
            next(self.iterator)
        except StopIteration:
            pass
        else:
            stream.annotate("Expected more output")
        self.post_finish_stream(stream)

    def post_finish_stream(self, stream):
        pass


class ParallelAnalyser(StreamAnalyser):
    """Runs multiple analysers in parallel."""

    def __init__(self, analysers):
        """Create a new ParallelAnalyser.

        analysers is a list of analysers to forward the callback calls to.
        The subanalyser's callbacks are called in the same order the
        subanalysers appear in the list of analysers.
        """
        self.analysers = analysers

    def start_stream(self, stream):
        for analyser in self.analysers:
            analyser.start_stream(stream)

    def analyse_line(self, line):
        for analyser in self.analysers:
            analyser.analyse_line(line)

    def finish_stream(self, stream):
        # TODO: Possibly, provide an option to invert the finish_stream calls.
        #       This might be useful for proper nesting of analysers.
        for analyser in self.analysers:
            analyser.finish_stream(stream)


class SkipTheRest(Exception):
    """An exception for skipping the rest of the stream.

    This Exception can be used to indicate to the analysers to skip the rest of
    the output.  If hide is True, no following lines will be printed out, but
    an ellipis instead.  f hide is false, all following lines will be printed
    grayed out.
    """

    # TODO: rename this class to StopAnalysis (similar to StopIteration)
    def __init__(self, message = "Skipping the rest", hide=False):
        super(SkipTheRest, self).__init__(self)
        self.message = message
        self.hide = hide


class SkipTheRestAnalyser(StreamAnalyser):
    """An Analyser for skipping lines.

    The SkipTheRestAnalyser works together with the SkipTheRest exception to
    skip all remaining output once a SkipTheRest exception was thrown.  The
    SkipTheRestAnalyser implements the decorator design pattern, and disables
    output of all remaining lines as soon as it catches a SkipTheRest
    exception.  Usually it is sufficient to have a SkipTheRestAnalyser wrapped
    around all other analysers, and tThis is already taken care of by the
    StreamAnalyser framework.  Sometimes it might also be useful to skip just
    the remaining lines of a block in a BlockAnayser and then continue printing
    with the next block.  In this case one can simple put a SkipTheRestAnalyser
    inside a BlockAnalyser.  This ensure the current Block is skipped, but the
    next block is then displayed again.
    """
    def __init__(self, analyser):
        """Create a new SkipTheRestAnalyser.

        analyser is the decorated StreamAnalyser.
        """
        self.analyser = analyser
        self.skip = False
        self.hide = False

    def start_stream(self, stream):
        try:
            self.analyser.start_stream(stream)
        except SkipTheRest as e:
            self.skip = True
            if e.hide:
                self.hide = True
            line.annotate_after(e.message, Severity.INFO)

    def analyse_line(self, line):
        if not self.skip:
            try:
                self.analyser.analyse_line(line)
            except SkipTheRest as e:
                self.skip = True
                if e.hide:
                    self.hide = True
                line.annotate_after(e.message, Severity.INFO)
        else:
            line.skip = True
            line.hide = True

    def finish_stream(self, stream):
        # TODO: make it configurable to also skip the finish annotations
        self.analyser.finish_stream(stream)


class SkipLinesAnalyser(StreamAnalyser):
    """Skip all lines matching a condition (whitespace-only by default).

    condition can either be a regular expression or function with signature
    string -> bool, which returns true iff the line should be skipped.
    """
    def __init__(self, analyser, condition = "^\s*$"):
        self.analyser = analyser
        self.condition = _ensure_callable(condition)

    def start_stream(self, stream):
        self.analyser.start_stream(stream)

    def analyse_line(self, line):
        if not self.condition(str(line)):
            self.analyser.analyse_line(line)

    def finish_stream(self, stream):
        self.analyser.finish_stream(stream)


class PrintAnalyser(StreamAnalyser):
    """Analyser for printing out analyses to the screen.

    The PrintAnalyser is not intended for creating new annotations, but for
    printing out existing annotations to a html document."""
    def __init__(self, document, css_class):
        """Create a new PrintAnalyser.

        document is the html.Document to be printed to. css_class is the css
        class of the output lines.  css_class could e.g. be blue for stdout
        output and red for stderr output.
        """
        self.document = document
        self.css_class = css_class

    def print_annotation(self, annotation):
        """print a single annotation"""
        with self.document.div({'class' : "severity"}):
            with self.document.span({'class' : "annotation " + annotation.severity}):
                self.document.cdata(str(annotation))

    def print_annotations(self, annotations):
        """print a list of annotations"""
        for annotation in annotations:
            self.print_annotation(annotation)

    def print_annotated_line(self, line):
        """Print a line with its annotations"""
        self.print_annotations(line.before.annotations)

        if not line.hide:
            attributes = { 'class' : self.css_class }
            if hasattr(line, 'css_class'):
                attributes['class'] = line.css_class

            with self.document.div(attributes):
                content = str(line)
                # TODO: enable printing of line numbers (need table layout for this, though)
                # document.cdata(str(line.number))
                if len(content) != 0:
                    self.document.cdata(str(line))
                else:
                    self.document.raw("&nbsp;")

        self.print_annotations(line.after.annotations)

    def start_stream(self, stream):
        self.print_annotations(stream.annotations)

    def analyse_line(self, line):
        self.print_annotated_line(line)

    def finish_stream(self, stream):
        self.print_annotations(stream.annotations)


class ExitAnalyser(object):
    """Base class for all exit analysers.

    An ExitAnalyser does not analyse a stream, but the returncode and run time
    of a program execution.

    Returncode indicates the return code of the program execution. A return
    code of None indicates a time out. time is the time taken for running the
    program. A time of None means time could not be measured.
    """
    def analyse_exit(self, prompt, returncode, time = None):
        pass


class TimeoutExitAnalyser(ExitAnalyser):
    """Identifies timeouts and prints out the time taken."""
    def analyse_exit(self, prompt, returncode, time = None):
        # check for timeout
        if returncode == None:
            prompt.annotate_after("Programm timed out", Severity.FAILURE)

        # print time if avaiable
        if time != None:
            time = "{0:.2f}".format(time)
            prompt.annotate_after("Time taken: " + time + "s", Severity.INFO)


class ExpectZeroExitAnalyser(TimeoutExitAnalyser):
    """Checks for a return code of zero and complains if not.

    If the program's retun code is not zero, this is interpreted as a failure.
    """
    def analyse_exit(self, prompt, returncode, time = None):
        super(ExpectZeroExitAnalyser, self).analyse_exit(prompt, returncode, time)

        # print and check the return code
        if returncode != 0:
            prompt.annotate_after("Programm exited with return code " + str(returncode))


def run_analyser(analyser, start, lines, finish):
    """Run a single analyser on the Annotateable start, the list of
    AnnotateableLines lines and finally the Annotateable finish.

    Returns Statistics about the number of collected messages of the different
    severities.
    """
    statistics = Statistics()

    # start stream
    analyser.start_stream(start)
    statistics += start

    # analyse lines
    for line in lines:
        analyser.analyse_line(line)
        statistics += line

    # finish stream
    analyser.finish_stream(finish)
    statistics += finish

    return statistics


def run_singlepass_analysis(document, lines, analysis, css_class):
    # we want a single analyser here, so wrap a list in a ParallelAnalyser
    # if necessary
    try:
        multiple = iter(analysis)
    except TypeError:
        analyser = analysis
    else:
        analyser = ParallelAnalyser(multiple)

    # add a SkipTheRest and a PrintAnalyser
    analyser = ParallelAnalyser([SkipTheRestAnalyser(analyser), PrintAnalyser(document, css_class)])

    def make_annotateable(lines):
        """Create AnnotateableLine objects on the fly"""
        for number, line in enumerate(lines):
            yield AnnotateableLine(line, number)

    # and run the analyser
    return run_analyser(analyser, Annotateable(), make_annotateable(lines), Annotateable())


def run_multipass_analysis(document, lines, analysis, css_class):
    # we want a list of analysers here, so wrap a single analyser in a list
    # if necessary
    try:
        multiple = iter(analysis)
    except TypeError:
        analysers = [ analysis ]
    else:
        analysers = multiple

    # add a SkipTheRest and a PrintAnalyser
    def with_printer(analysers, document, css_class):
        for analyser in analysers:
            yield SkipTheRestAnalyser(analyser)
        yield PrintAnalyser(document, css_class)

    analysers = with_printer(analysers, document, css_class)

    # run the analysis
    statistics = Statistics()

    start = Annotateable()
    lines = [AnnotateableLine(line, number) for number, line in enumerate(lines)]
    finish = Annotateable()

    for analyser in analysers:
        statistics += run_analyser(analyser, start, lines, finish)

    return statistics


def run_bestof_analysis(document, lines, analysis, css_class):
    # we want a list of analysers here, so wrap a single analyser in a list
    # if necessary
    try:
        multiple = iter(analysis)
    except TypeError:
        analysers = [ analysis ]
    else:
        analysers = multiple

    # run the analysis
    best_start = None
    best_lines = None
    best_finish = None
    best_statistics = None

    # try them all out
    for analyser in (SkipTheRestAnalyser(analyser) for analyser in analysers):
        start = Annotateable()
        anno_lines = [AnnotateableLine(line, number) for number, line in enumerate(lines)]
        finish = Annotateable()
        statistics = run_analyser(analyser, start, anno_lines, finish)

        def is_better(new, old):
            if new.crashes < old.crashes:
                return True
            elif old.crashes < new.crashes:
                return False
            if new.failures < old.failures:
                return True
            elif new.failures > old.failures:
                return False
            elif new.warnings < old.warnings:
                return True
            elif new.warnings > old.warnings:
                return False
            else:
                return False


        # and update the best result, if the new one is better
        if not best_statistics or is_better(statistics, best_statistics):
            best_statistics = statistics
            best_start = start
            best_lines = anno_lines
            best_finish = finish

    # now print the best run
    best_statistics += run_analyser(PrintAnalyser(document, css_class), best_start, best_lines, best_finish)
    return best_statistics


class Mode(object):
    """Mode in which StreamAnalysers can be run.

    SINGLEPASS
       Run all StreamAnalysers in one go.

    MULTIPASS
       Run each StreamAnalyser in its own pass.

    BESTOF
       Run each StreamAnalyser, but only display one of them.
       Which one to print is chosen by the following rules:

    For the mode BESTOF, the following rules are applied:
    - The analysers with the fewest crashes is displayed.

    - If these all have the same number of crashes, then out of this group,
      the analyser with the fewest failures is displayed.

    - If these all have the same number of failures, then out of this
      group, the analysers with the fewest warnings is displayed.

    - If these all have the same number of warnings, then out of this group,
      the first analyser in the list is displayed.

    MULTIPASS is good if information from later lines is needed for analysing
    earlier lines. BESTOF is useful whenever two or more correct
    interpretations of a task are possible, but cannot easily be put in one
    Analyser. This is e.g. the case when it is ok to print out an error
    message, but there is also an acceptable way to treat the situation and
    continue with the algorithm.

    """
    SINGLEPASS = 1
    MULTIPASS = 2
    BESTOF = 3


def analyse_and_print_stream(document, lines, analysers, css_class = "", mode=Mode.SINGLEPASS):
    """Run a list of StringAnalysers on a list of lines.

    Use css_class to determine the look of the lines themselves (e.g. blue for
    the output stream, red for the error stream)."""
    if mode == Mode.SINGLEPASS:
        return run_singlepass_analysis(document, lines, analysers, css_class=css_class)
    elif mode == Mode.MULTIPASS:
        return run_multipas_analysis(document, lines, analysers, css_class=csss_class)
    elif mode == Mode.BESTOF:
        return run_bestof_analysis(document, lines, analysers, css_class=css_class)
    else:
        raise ConfigurationError("Invalid analysis mode")


def analyse_and_print_exit(document, returncode, time, analyser):
    """Run the analyser on the returncode and the time."""
    if not analyser:
        analyser = ExpectZeroExitAnalyser()

    statistics = Statistics()

    prompt = AnnotateableLine(">")
    analyser.analyse_exit(prompt, returncode, time)
    PrintAnalyser(document, "prompt").analyse_line(prompt)

    statistics += prompt
    return statistics


def error_analysers():
    """Return analysers if an error message is expected."""
    return {
        'stdout' : MissingMainClassAnalyser(ExceptionAnalyser(ErrorAnalyser())),
        'stderr' : MissingMainClassAnalyser(ExceptionAnalyser()),
        'exit' : TimeoutExitAnalyser()
    }

def stability_analysers():
    """Return analysers for checking if the submission crashed somehow."""
    return {
        'stdout' : MissingMainClassAnalyser(ExceptionAnalyser()),
        'stderr' : MissingMainClassAnalyser(ExceptionAnalyser()),
        'exit' : TimeoutExitAnalyser()
    }
