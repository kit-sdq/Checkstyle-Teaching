"""The module test contains the class Test, representing a single test case."""

import contexts
from exc import ConfigurationError
from exc import StudentFailure
from exc import StudentWarning
from severity import Severity
from output import print_text


class Test(object):
    """A test represents a single test to be run.

    A test always requires a title to be set, and always has a result.  The result
    by default is Severity.SUCCESS.  It can optionally have a description.
    A test is displayed as a box with a header containing its name (and
    optionally a link to an extenal url) and a footer indicating its result (by
    text and by color).

    A test needs to be run in a checker and will automatically add its own
    result to the checkers summary.

    A test is intended to be used like this::

       test = Test(checker, "Title", description="Example test", url="http://python.org")
       with test.context():
          test.initialize()

          # do something with the test.
    """

    def __init__(self, checker, title, description=None, url=None):
        """Create a new Test object.
        
        checker
           Used to add the test's result to a checker's list of results.

        title
           The Test's title (First letter is capitalized, the rest is lower case).

        description
           A short description of the test displayed in a first section.

        url
           If not None, the title becomes a clickable link to this url.
        """
        self.checker = checker
        self.document = checker.document
        self.title = title.capitalize()
        self.description = description
        self.url = str(url) if url else None
        self.stack = contexts.ExitStack()
        self.severity = Severity.SUCCESS
        self.message = None

    def context(self):
        return self.stack


    def initialize(self):
        self.stack.enter_context(self.document.div({'class' : "outer"}))
        self.stack.enter_context(self)
        self.stack.enter_context(self.document.div({'class' : "inner insetshadow", 'style' : "display:none" }))

        if self.description:
            print_text(self.document, "Description", self.description)


    def __enter__(self):
        with self.document.h4():
            with self.document.span({'class':"showhide", 'onClick':"javascript:toggle_visibility(this, this.parentNode.nextSibling);"}):
                self.document.cdata("+")
            if self.url:
                with self.document.a({'href' : self.url, 'target' : "_blank"}):
                    self.document.cdata(self.title)
            else:
                self.document.cdata(self.title)


    def __exit__(self, exc_type, exc_value, exc_tb):
        # catch the different types of exceptions here
        # set severity and message accordingly
        misconfiguration = False
        if isinstance(exc_value, StudentFailure):
            self.severity = Severity.FAILURE
            self.message = str(exc_value)
        elif isinstance(exc_value, StudentWarning):
            self.severity = Severity.WARNING
            self.message = str(exc_value)
        elif isinstance(exc_value, ConfigurationError):
            self.severity = Severity.CRASH
            self.message = str(exc_value)
            misconfiguration = True
        elif exc_value:
            self.severity = Severity.CRASH
            self.message = exc_type.__name__ + ": " + str(exc_value)

        # create status message
        severity_string = Severity.string(self.severity)
        status = severity_string.capitalize() if not misconfiguration else "Misconfiguration"
        if self.message: status += ": " + self.message

        # if this is a real crash (not a misconfiguration) display a stack trace
        if self.severity == Severity.CRASH and not misconfiguration:
            from traceback import format_tb
            with self.document.div({'class' : 'crashexplanation', 'style' : 'display:none'}):
                self.document.cdata("\n".join(format_tb(exc_tb)))

        # display status colored by severity
        with self.document.div({'class' : 'severity'}):
            with self.document.div({'class' : 'result ' + severity_string}):
                if self.severity == Severity.CRASH and not misconfiguration:
                    with self.document.span({'class' : "showhide", 'onClick' :
                        "javascript:toggle_visibility(this, this.parentNode.parentNode.previousSibling);"}):
                        self.document.cdata("+")
                self.document.cdata(status)

        # and count result into the checker
        self.checker.add_result(self.severity)

        self.document.flush()

        # we swallow all exceptions here
        return True
