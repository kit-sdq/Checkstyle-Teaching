"""The ipo module provides means for easily checking the output of IPO programs.

IPO programs follow the input-process-output paradigm. These progams are
non-interactive and operate in three phases: input, processing, and output.
The are easier to test than interactive programs, because the streams don't
need to be interleaved properly.
"""
import logging
import os

from . import contexts
from . import output
from . import ressources
from . import submission

from .analysers import analyse_and_print_exit
from .analysers import analyse_and_print_stream
from .analysers import MissingMainClassAnalyser
from .analysers import ExceptionAnalyser
from .analysers import Mode
from .checker import Checker
from .exc import StudentFailure
from .exc import ConfigurationError
from .severity import Statistics
from .test import Test

import json
class StructuredMessage(object):
    def __init__(self, **kwargs):
        self.kwargs = kwargs

    def __str__(self):
        return json.dumps(self.kwargs)

def print_and_check_execution(document, commandline, stdin = [], stdout = [], stderr = [], analysers = {}, returncode=None, time=None, mode=Mode.SINGLEPASS):
    """Print out the execution of a command line program"""
    statistics = Statistics()

    with document.h5():
      document.cdata("Program execution:")

    with document.div({'class' : "section"}):
        with document.div({'class' : "window"}):
            with document.div({'class' : "decoration title"}):
                document.cdata("Terminal")

            with document.div({'class' : "console"}):
                with document.div({'class':"prompt"}):
                    document.cdata("> " + commandline)

                for line in stdin:
                    with document.div({'class':"stdin"}):
                        document.cdata(line)

                # print and check standard output
                stdout_analysers = analysers.get('stdout', MissingMainClassAnalyser(ExceptionAnalyser()))
                statistics += analyse_and_print_stream(document, stdout, stdout_analysers, css_class = "stdout", mode=mode)

                # print and check standard error
                stderr_analysers = analysers.get('stderr', MissingMainClassAnalyser(ExceptionAnalyser()))
                statistics += analyse_and_print_stream(document, stderr, stderr_analysers, css_class = "stderr")

                # print and check exit
                exit_analyser = analysers.get('exit')
                statistics += analyse_and_print_exit(document, returncode, time, exit_analyser)

            with document.div({'class' : "decoration status"}):
                output.print_legend(document)

    if not statistics.crashes == 0:
        raise ConfigurationError("Encountered " + str(statistics.crashes) + " crashes" if statistics.crashes != 1 else "Encountered a crash")

    if not statistics.failures == 0:
        raise StudentFailure("Found " + str(statistics.failures) + " serious issues" if statistics.failures != 1 else "Found a serious issue")

    if not statistics.warnings == 0:
        raise StudentWarning("Found " + str(statistics.warnings) + " issues" if statistics.warnings != 1 else "Found an issue")


def run_single_test(checker, description):
    """Run a single test described in the string test_description"""
    # read in the description

    # arguments
    arguments = description.get("arguments", "")

    # environment
    environment = description.get("environment", {})

    #stdin
    stdin = description.get("stdin", [])
    stdin = [stdin] if isinstance(stdin, str) else stdin
    
    classpath = description.get("classpath", [])
    
    # files (the ExitStack ensures the temporary files are cleaned up after use)
    with contexts.ExitStack() as tempfiles:
        files = description.get('files', {})

        # treat protocols as hardcoded temporary files
        for desc, path in list(description.items()):
            if 'protocol' in desc:
                files[desc + '.txt'] = ressources.read(path)
        
        # create temporary files in the sandbox
        for filename, content in list(files.items()):
            path = os.path.join("sandbox", filename)
            descriptor = open(path, 'w')
            tempfiles.enter_context(descriptor)
            descriptor.write(content.decode())
            descriptor.flush()

            # show contents of temporary files (except for protocol files)
            if not 'protocol' in filename:
                output.print_file(checker.document, filename, content)

        # run the submission
        stdout,stderr,returncode,time = submission.execute_submission(
            checker.java,
            checker.main,
            environment=environment,
            arguments=arguments,
            stdin=stdin,
            timeout=description.get("timeout", 10),
            classpath=classpath
        )

        # analyse (and print) the output
        failed = False
        try:
            print_and_check_execution(
                checker.document,
                "java " + checker.main + " " + arguments,
                stdin = stdin,
                stdout = stdout,
                stderr = stderr,
                returncode = returncode,
                time = time,
                analysers = description.get("analysers", {}),
                mode = description.get("mode", Mode.SINGLEPASS)
            )
        except StudentFailure as e:
            failed = True
            #rethrow exception
            raise e
        finally:
            #log test result
            import __main__ as main
            script_name = os.path.basename(main.__file__)
            id = description.get("name") if not description.get("id") else description.get("id") 
            logging.info(StructuredMessage(id=id, checker = script_name, name = description.get("name"), failed = failed, stdout = stdout, stderr = stderr))

def run_multiple_tests(checker, configuration):
    """Run all tests stored in a zip file"""
    for description in configuration:
        test = Test(checker, description.get("name", "Test"), description.get("description", None))
        with test.context():
            test.initialize()
            run_single_test(checker, description)


def run(files, configuration, **kwargs):
    """Run an input-process-output program and analyse its output."""

    checker = Checker(files=files, **kwargs)
    with checker.context():
        checker.initialize()

        if os.path.exists("sandbox"):
            # timeouts of previous checkers might leave a sandbox directory here,
            # so we don't raise an exception but simply delete it
            # bad luck if there's parts of a student's submission in there...
            import shutil
            shutil.rmtree("sandbox", ignore_errors=True)

        with contexts.NamedTempDir("sandbox"):
            run_multiple_tests(checker, configuration)

    return checker.result
