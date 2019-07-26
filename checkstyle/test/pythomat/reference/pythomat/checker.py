import os
import re
import sys

import contexts
from severity import Severity
from severity import Statistics
from exc import StudentFailure
from exc import ConfigurationError
from output import print_statistics


__package_re = re.compile('(\/\*((?s).*?)\*\/|\/\/.*)*\s*package\s+(.*?);')
def identify_package(filename):
    with open(filename, 'r') as javafile:
        m = __package_re.search(javafile.read())
        if m:
            return m.group(3)


__main_re = re.compile('(public\s+static|static\s+public)\s+void\s+main\s*\(')
def contains_main_method(filename):
    """search for the main method in the file"""
    with open(filename, 'r') as javafile:
        for line in javafile:
            if __main_re.search(line):
                return True


def search_main_method(filenames, excludemain):
    """search for a class containing a main method"""
    # open all java files in the directory
    # search them for a file containing a main method
    # return the fully qualified name of that class
    for filename in (filename for filename in filenames if filename.lower().endswith(".java")):
        if contains_main_method(filename):
            classname = os.path.basename(os.path.splitext(os.path.basename(filename))[0])
            # skip potential main class if is excluded
            if classname == excludemain:
                continue
            package = identify_package(filename)
            if package:
                return package + "." + classname
            else:
                return classname
    return None


def contains_class_definition(classname, filename):
    __classdef_re = re.compile("(abstract|final|public|protected|\s)*class\s*" + classname + "\s.*")
    with open(filename, 'r') as javafile:
        for line in javafile:
            if __classdef_re.match(line):
                return True


def search_class_definition(classname, filenames):
    for filename in (filename for filename in filenames if filename.lower().endswith(".java")):
        if contains_class_definition(classname, filename):
            classname = os.path.basename(os.path.splitext(os.path.basename(filename))[0])
            package = identify_package(filename)
            if package:
                return package + "." + classname
            else:
                return classname
    return None


class Checker(object):
    """Checker represents a Praktomat checker.

    A Checker is a context manager that provides a context for running tests
    in. It gives access to the java executable, the student's submission. It
    initialized the HTML document to be used and tries to make sure no one
    elses uses the sys.stdout stream unguarded.

    Once the Checker is initialized, one or multiple tests can be run. The
    results. Tests can set counts for failures, warnings etc.

    When the context manager is exited, it will print out a summary of the
    results to the HTML document.

    Use it roughly like this::

       checker = Checker()
       with checker.context():
          checker.initialize()

          for test in tests:
             if test.failed:
                checker.add_result(Severity.FAILURE) += 1
             else:
                testl.add_result(Severity.SUCCESS) += 1

    """
    def __init__(self, **kwargs):
        self.document = None
        self.statistics = Statistics()
        self.stack = contexts.ExitStack()
        self.description = kwargs.get('description')

        # try to determine the path to the java executable
        if 'java' in kwargs:
            self._java_secure = kwargs['java']
        elif os.environ.has_key("JAVA_SECURE"):
            self._java_secure = os.environ["JAVA_SECURE"]

        # try to determine the path to the insecure java executable
        if 'java_insecure' in kwargs:
            self._java_insecure = kwargs['java_insecure']
        elif os.environ.has_key("JAVA"):
            self._java_insecure = os.environ["JAVA"]

        # list all java files (the main method might not be in one of the submitted files)
        all_java_files = []
        for d, ss, fs in os.walk(os.getcwd()):
            for f in fs:
                if os.path.splitext(f)[1] == ".java":
                    all_java_files.append(os.path.join(d,f))

        # try to determine the name of the class containing the main method
        if 'main' in kwargs:
            # see if it was passed in explicitly, try to make it fully qualified
            self._main_class = search_class_definition(kwargs['main'], all_java_files) or kwargs['main']
        elif os.environ.has_key("PROGRAM") and not 'excludemain' in kwargs:
            # TODO: excludemain should better be compared to the defined environment variable 
            # to ensure that the latter does not refer to the main class to be excluded/ignored.
            # see if there's an environment variable defined
            self._main_class = os.environ["PROGRAM"]
        elif 'files' in kwargs:
            # see if we can find it in the list of submitted java files
            # TODO: search the main method in all java files in the working
            #       directory or any of its subdirectories
            main_method = search_main_method(all_java_files, kwargs['excludemain'])
            if main_method:
                self._main_class = main_method

    @property
    def java(self):
        """The path to the java executable to be used for running student submissions."""
        try:
            return self._java_secure
        except:
            raise ConfigurationError("The path to the (secure) java is not set propertly")

    @property
    def java_insecure(self):
        """The path to the java executable, but without a security policy in place."""
        try:
            return self._java_insecure
        except:
            raise ConfigurationError("The path to the (insecure) java executable is not set properly")

    @property
    def main(self):
        """The name of the main class (if there is one) in the student's submission.

        If there is more than one class containing a main method it is
        undefined which one of them will be returned.

        The method for finding the main class is not particularly elaborate in
        identifying the right class and can fail if there is something looking
        like a main method in a comment or a string in one of the source files.
        """
        try:
            return self._main_class
        except:
            raise ConfigurationError("The name of the main class is not configured")

    def context(self):
        """The context manager of this Checker."""
        return self.stack

    def initialize(self):
        """Set the checker up, including the HTML document etc."""
        # first install the output buffer...
        self.stack.enter_context(contexts.BufferedStdoutGuard())
        # then initialize the document so it writes to the buffer...
        from html import Document
        self.document = Document(sys.stdout)
        self.stack.enter_context(self.document)
        # and then make sure non one else writes to stdout or stderr
        self.stack.enter_context(contexts.NoStdoutGuard())
        self.stack.enter_context(contexts.NoStderrGuard())

        toggle_script = """
function toggle_visibility(button, element) {
    if ($(button).text() == '-') {
        $(element).slideUp();
        button.innerHTML = '+';
    } else {
        $(element).slideDown();
        button.innerHTML = '-';
    }
}"""
        with self.document.javascript():
            self.document.raw(toggle_script)

        from css import inject_pythomat_css
        inject_pythomat_css(self.document)
        # explicitly flush after css, because in mid-css is an especially bad place to stop emitting html
        self.document.flush()

        # provide the html frame for pythomat
        self.stack.enter_context(self.document.div({'class' : "pythomat"}))
        if self.description:
            with self.document.div({'class' : 'outer insetshadow'}):
                with self.document.div({'class' : 'inner'}):
                    with self.document.div({'class' : 'section'}):
                        self.document.raw(self.description)
        self.stack.enter_context(ResultPrinter(self))

    def add_result(self, severity):
        """Add another result to the checker."""
        self.statistics.add(severity)

    @property
    def result(self):
        """Return the Checker's result.

        A checker's run is only successful if there is an explicit success and
        no failures.
        """
        return self.statistics.result


class ResultPrinter(object):
    """Catch exceptions and print them out at the end of the document or document fragment."""
    def __init__(self, checker):
        self.checker = checker


    def __enter__(self):
        pass


    def __exit__(self, exc_type, exc_value, exc_tb):
        document = self.checker.document
        statistics = self.checker.statistics

        # special case StudentFailure and other exceptions
        if isinstance(exc_value, StudentFailure):
            if statistics.failures == 0:
                statistics.failures = 1

            with document.div({'class' : 'message'}):
                with document.div({'class' : 'failure'}):
                    document.cdata("Failure: " + str(exc_value))

        elif isinstance(exc_value, ConfigurationError):
            if statistics.crashes == 0:
                statistics.crashes = 1

            with document.div({'class' : 'message'}):
                with document.div({'class' : 'failure'}):
                    document.cdata("Misconfiguration: " + str(exc_value))

        elif exc_type:
            if statistics.crashes == 0:
                statistics.crashes = 1

            from traceback import format_tb
            with document.div({'class' : 'message'}):
                with document.div({'class' : 'crash'}):
                    document.cdata("Crash: " + str(exc_value))
                with document.div({'class' : 'crash'}):
                    document.cdata("Please contact your trainer about this.")
                with document.pre({'class' : 'crash'}):
                    document.cdata("\n".join(format_tb(exc_tb)))

        print_statistics(document, statistics)

        # we printed out messages about the exceptions already, so we can
        # swallow them here
        return True 
