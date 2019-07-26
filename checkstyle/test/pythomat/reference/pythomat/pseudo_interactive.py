import cgi
import itertools
import json
import os.path
import subprocess
import sys
import traceback
import zipfile

import css
import html
import contexts
import output
import submission

from checker import Checker
from test import Test
from exc import StudentFailure
from exc import ConfigurationError


def analyze_error_message(document, commandline, input_lines, output_lines, expected_message):
    """Check if the returned error message is as expected."""
    with document.h5():
      document.cdata("Program execution:")

    with document.div({'class' : "window"}):
        with document.div({'class' : "decoration title"}):
            document.cdata("Terminal")

        with document.div({'class' : "console"}):
            with document.div({'class':"prompt"}):
                document.cdata("> " + commandline)

            an_error = True if expected_message.match(output_lines[0].strip()) else False

            color = "#008300" if an_error else "#FF0000"

            for pair in itertools.izip_longest([x.strip() for x in input_lines], [x.strip() for x in output_lines], ["Error, ..."], fillvalue=""):
                with document.div({'class':"stdin"}):
                    document.cdata(pair[0])

                with document.div({'class':"stdout"}):
                    document.cdata(pair[1])

                if an_error:
                    with document.div({'class':"annotation-line"}):
                        with document.span({'class':"annotation-shape"}):
                            document.cdata(pair[2])

            with document.div({'class':"prompt"}):
                document.cdata(">")

        with document.div({'class' : "decoration status"}):
            output.print_legend(document)

    if not an_error:
        raise StudentFailure("Not the expected output")


def analyze_regular_output(document, commandline, input_lines, output_lines, expected_lines):
    """Check if the generated output is as expected."""
    with document.h5():
      document.cdata("Program execution:")

    with document.div({'class' : "window"}):
        with document.div({'class' : "decoration title"}):
            document.cdata("Terminal")

        with document.div({'class' : "console"}):
            with document.div({'class':"prompt"}):
                document.cdata("> " + commandline)

            document_expected = True
            for pair in itertools.izip_longest([x.strip() for x in input_lines], [x.strip() for x in output_lines], [x.strip() for x in expected_lines], fillvalue=""):

                with document.div({'class':"stdin"}):
                    document.cdata(pair[0])

                with document.div({'class':"stdout"}):
                    document.cdata(pair[1])

                line_expected = (pair[1] == pair[2])
                if not line_expected:
                    with document.div({'class':"annotation-line"}):
                        with document.span({'class':"annotation-shape"}):
                            document.cdata(pair[2] + " was expected")

                document_expected &= line_expected

            with document.div({'class':"prompt"}):
                document.cdata(">")

        with document.div({'class' : "decoration status"}):
            output.print_legend(document)

    # do the files match?
    if not document_expected:
        raise StudentFailure("Output doesn't match the expected output")


def run_single_test(checker, test_json):
    """Run a single test described in the string test_description."""

    # read in input lines from test description
    stdin = test_json["input"] if "input" in test_json else ""
    if isinstance(stdin, basestring):
        # make it a list, if it isn't yet
        stdin = [ stdin ]

    arguments = test_json["arguments"] if "arguments" in test_json else ""

    # execute the submission with the arguments. pass in input, compare to
    # output
    stdout,stderr,returncode,time = submission.execute_submission(
        checker.java,
        checker.main,
        arguments=arguments,
        stdin=stdin
    )

    commandline = "java " + checker.main + " " + arguments
    if "expect" in test_json:
        # run a regular 'diff' test on the output
        expected_lines = test_json["expect"]
        if isinstance(expected_lines, basestring):
            expected_lines = [ expected_lines ]
        analyze_regular_output(checker.document, commandline, stdin, stdout, expected_lines)
    elif "error" in test_json:
        # check for an error message, but be relaxed about it
        error_message = test_json["error"]
        analyze_error_message(checker.document, commandline, stdin, stdout, re.compile(error_message))
    else:
        raise ConfigurationError("Cannot run test: Expected behaviour is unspecified.")


def extract_testcases(zip_filename):
    """Extract the test cases from the zip file."""

    if not zipfile.is_zipfile(zip_filename):
        raise ConfigurationError(zip_filename + " is not a valid zip file")

    zip_file = zipfile.ZipFile(zip_filename, 'r')
    test_files = zip_file.namelist()

    for test_file in test_files:
        # skip anything not ending in '.test'
        if os.path.splitext(test_file)[1] != ".test":
            continue

        test_json = json.loads(unicode(zip_file.read(test_file)))

        if not "name" in test_json:
            test_json["name"] = test_file

        yield test_json


def run_all_tests(checker, tests):
    """Run all tests stored in a zip file."""
    for test in tests:
        testcase = Test(checker, test["name"])
        with testcase.context():
            testcase.initialize()

            run_single_test(checker, test)


def run(files, zip_filename, **kwargs):

    checker = Checker(files=files, **kwargs)
    with checker.context():
        checker.initialize()

        test_cases = extract_testcases(zip_filename)

        if os.path.exists("sandbox"):
            # timeouts of previous checkers might leave a sandbox directory here,
            # so we don't raise an exception but simply delete it
            # bad luck if there's parts of a student's submission in there...
            shutil.rmtree("sandbox", ignore_errors=True)

        with contexts.NamedTempDir("sandbox"):
            run_all_tests(checker, test_cases)

    return checker.result
