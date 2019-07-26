import os.path

from . import analysers
from . import contexts
from . import ipo
from . import submission

from .analysers import SkipTheRest
from .checker import Checker
from .exc import StudentFailure
from .exc import ConfigurationError
from .severity import Severity


class InteractiveAnalyser(analysers.StreamAnalyser):
    def __init__(self):
        pass

    def start_stream(self, stream):
        pass

    def analyse_line(self, anno_line):
        # ensure protocol is displayed first, only then display user
        # then we can work on the user line by line
        # just need to skip the protocol and our own stuff somehow
        # process the output here

        if len(str(anno_line)) == 0:
            return

        try:
            tag, line = str(anno_line).split(':', 1)
        except:
            anno_line.annotate_after("Please use Terminal instead of System.out")
            raise analysers.SkipTheRest(hide=True)

        # remove the tag from the output
        anno_line.substitute = line

        if tag == "prompt":
            anno_line.css_class = 'stdout'

        elif tag == "input":
            anno_line.css_class = 'stdin'

        elif tag == "output":
            anno_line.css_class = 'stdout'

        elif tag == "crash":
            anno_line.hide = True
            anno_line.annotate_before(line, Severity.CRASH)
            raise SkipTheRest(hide=True)

        elif tag == "error":
            anno_line.hide = True
            anno_line.annotate_before(line, Severity.FAILURE)
            #raise SkipTheRest(hide=False)

        elif tag == "warning":
            anno_line.hide = True
            anno_line.annotate_before(line, Severity.WARNING)

        elif tag == "info":
            anno_line.hide = True
            anno_line.annotate_before(line, Severity.INFO)

        elif tag == "exit":
            anno_line.hide = True

        elif tag == "term":
            anno_line.hide = True
            anno_line.annotate_before(line, Severity.INFO)
            raise SkipTheRest(hide=True)

        else:
            anno_line.annotate_before("Please use Terminal instead of System.out")
            raise SkipTheRest(hide=True)


class TerminalUsageAnalyser(analysers.StreamAnalyser):
    """Checks if the terminal class has been invoked at all. This is the case when the protocol is empty."""
    
    lines = 0
    
    def __init__(self, analyser):
        self.analyser = analyser

    def start_stream(self, stream):
        self.analyser.start_stream(stream)

    def analyse_line(self, line):
        self.lines += 1
        self.analyser.analyse_line(line)

    def finish_stream(self, stream):
        # protocol empty?
        if self.lines == 0:
            stream.annotate("Program terminated without processing any input, or generating any output.")
        self.analyser.finish_stream(stream)


class IgnoreReturnCodeAnalyser(analysers.TimeoutExitAnalyser):
    """Accepts any return code"""

    def analyse_exit(self, prompt, returncode, time = None):
        super(IgnoreReturnCodeAnalyser, self).analyse_exit(prompt, returncode, time)


def run(files, configuration, **kwargs):

    for test in configuration:
        if 'analysers' in test:
            raise ConfigurationError("Don't add your own analysers to interactive tests")

        test['analysers'] = {
            'stdout' : analysers.MissingMainClassAnalyser(analysers.ExceptionAnalyser(TerminalUsageAnalyser(InteractiveAnalyser()))),
            'stderr' : analysers.MissingMainClassAnalyser(analysers.ExceptionAnalyser()),
            'exit'   : IgnoreReturnCodeAnalyser()
        }

    checker = Checker(files=files, **kwargs)
    with checker.context():
        checker.initialize()

        if os.path.exists("sandbox"):
            # timeouts of previous checkers might leave a sandbox directory here,
            # so we don't raise an exception but simply delete it
            # bad luck if there's parts of a student's submission in there...
            from shutil import rmtree
            rmtree("sandbox", ignore_errors=True)

        with contexts.NamedTempDir("sandbox"):
            ipo.run_multiple_tests(checker, configuration)

    return checker.result
