"""A collection of methods for printing higher-level widgets to an html
document.
"""
from severity import Severity

def print_text(document, title, text):
    """Print a section of text."""

    with document.h5():
        document.cdata(title + ":")

    with document.div({'class' : 'section'}):
        document.cdata(text)


def print_file(document, filename, content):
    """Print out file's contents on a (fake) piece of paper."""

    if isinstance(content, str):
        content = content.split("\n")

    with document.h5():
        document.cdata("Input file ")
        with document.span({'style' : 'font-family:monospace'}):
            document.cdata(filename)
        document.cdata(":")

    with document.div({'class' : 'section'}):
        with document.div({'class' : "paper"}):
            for line in content:
                with document.div():
                    document.cdata(line)


def print_execution(document, commandline, stdin = [], stdout = [], stderr = []):
    """Print out the execution of a command line program in a (fake) terminal."""
    with document.h5():
      document.cdata("Program execution:")

    with document.div({'class' : 'section'}):
        with document.div({'class' : "window"}):
            with document.div({'class' : "decoration title"}):
                document.cdata("Terminal")

            with document.div({'class' : "console"}):
                with document.div({'class':"prompt"}):
                    document.cdata("> " + commandline)

                for line in stdin:
                    with document.div({'class':"stdin"}):
                        document.cdata(line)
                for line in stdout:
                    with document.div({'class':"stdout"}):
                        document.cdata(line)
                for line in stderr:
                    with document.div({'class':"stderr"}):
                        document.cdata(line)

                with document.div({'class':"prompt"}):
                    document.cdata(">")

            with document.div({'class' : "decoration status"}):
                print_legend(document)


def print_legend(document):
    """Print out a legend for a terminal windows.
    
    Displays the colors used for input, output, and error streams."""
    with document.div({'class' : "legend"}):
        with document.span({'class':"stdin box"}):
            pass
        with document.span():
            document.cdata("System.in")

        with document.span({'class':"stdout box"}):
            pass
        with document.span():
            document.cdata("System.out")

        with document.span({'class':"stderr box"}):
            pass
        with document.span():
            document.cdata("System.err")


class Message(object):
    """Abstract representation of an error or warning message."""
    def __init__(self, severity, message, location = None):
        self.severity = severity
        self.location = location
        self.message = message


def print_messages(document, messages, title = None):
    """Print the messages to the document"""
    if not messages:
        return

    if title:
        with document.h5():
            document.cdata(title + ":")

    with document.div({'class' : 'section'}):
        for message in messages:
            severity = Severity.string(message.severity)

            with document.div({'class' : 'message'}):
                with document.div({'class' : severity}):
                    if message.location:
                        with document.span({'class':"location"}):
                            document.cdata(message.location + ": ")
                    document.cdata(message.message)


def print_statistics(document, statistics):
    """Print out bar illustrating a test's or checker's statistics.
    
    This prints a bar that looks like a progress bar indicating how many tests
    or checks were successfull, how many failed, how many crashed, etc."""
    if statistics.total == 0:
        # no results? then fake a success
        with document.div():
            document.cdata("Nothing was accomplished here")

        statistics.successes = 1

    # print a progress bar indicating the number of successes and failures
    with document.div({'class' : 'outer'}):
        with document.div({'class' : 'severity progressbar'}):
            def style(cls, percent, first, last):
                result = { 'style' : "width:" + str(percent) + "%" }
                full_cls = cls
                if first: full_cls += " first"
                if last: full_cls += " last"

                result['class'] = full_cls

                return result

            # walk over all severity levels and display them in the
            # progress bar
            for severity in Severity.iterate():
                count = statistics.count(severity)
                if count != 0:
                    percentage = statistics.percentage(severity)

                    # adapt rounded edges as needed
                    is_first = sum(statistics.count(other) for other in Severity.iterate() if other < severity) == 0
                    is_last = sum(statistics.count(other) for other in Severity.iterate() if other > severity) == 0
                    cls = Severity.string(severity)
                    if is_first: cls += " first"
                    if is_last: cls += " last"
                    label = Severity.plural(severity) if count > 1 else Severity.string(severity)

                    # print part of the progress bar
                    with document.span({'class' : cls, 'style' : "width: " + str(percentage) + "%"}):
                        document.cdata(str(count) + " " + label)

