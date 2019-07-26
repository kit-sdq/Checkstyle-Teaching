import itertools
import os.path
import re
import subprocess
import sys
import traceback
import zipfile

from pythomat import css
from pythomat import html
from pythomat import contexts
from pythomat import checker
from pythomat import output
from pythomat import submission

from pythomat.checker import Checker
from pythomat.exc import StudentFailure
from pythomat.output import Message
from pythomat.severity import Severity
from pythomat.test import Test


def check_errors(stdout, message):
    """Check that the output starts with the expected error message"""
    if not stdout[0].strip().startswith(message):
        yield Message(Severity.FAILURE, "Expected \"Keine Belegung gefunden.\"", "Line 1")


def check_row_count(stdout, rows):
    """Check if the number of rows is as expected"""
    if len(stdout) != rows:
            yield Message(Severity.FAILURE, "Expected " + str(rows) + " lines, but got " + str(len(stdout)))


def check_column_count(stdout, columns):
    """Check if the number of columns in all lines is as expected"""
    for num, line in enumerate(stdout):
        if len(line) != columns:
            yield Message(Severity.FAILURE, "Expected line width of " + str(columns) + ", but got " + str(len(line)), "Line " + str(num+1))


def check_characters(stdout, allowed):
    """Check that the output contains only the allowed characters"""
    for row, line in enumerate(stdout):
        for col, char in enumerate(line):
            if not char in allowed:
                yield Message(output.Message.error, "Unexpected character " + char, "Line " + str(row) + ", column " + str(col) + ":")


def check_attacks(stdout, size):
    """Check if one of the queens can attack another one"""

    # check the rows
    for num, line in enumerate(stdout[0:size]):
        if line.count("*") > 1:
            yield Message(Message.error, str(line.count("*")) + " queens in a single row", "Line " + str(num+1))

    # check the columns
    for num, column in enumerate(zip(*stdout)):
        if column.count("*") > 1:
            yield Message(Message.error, str(column.count("*")) + " queens in a single column", "Column " + str(num+1))

    # check the first diagonal
    found_downwards = False
    for p in range(0, 2*size-1):
        for q in range(max(0,p - size + 1), min(p, size - 1)):
            if stdout[q][p-q] == '*':
                if not found_downwards:
                    found_downwards
                else:
                    yield Message(Message.error, "Too many queens on a diagonal")

    # check the other diagonal
    found_upwards = False
    for p in range(0, 2*size-1):
        for q in range(max(0,p - size + 1), min(p, size - 1)):
            if stdout[size - 1 - q][p - q] == '*':
                if not found_upwards:
                    found_upwards
                else:
                    yield Message(Message.error, message="Too many queens on a diagonal")


def check_count(stdout, char, size):
    """Check if there is the right number of queens on the board"""
    count = sum([line.count(char) for line in stdout])
    if count != size:
        yield Message(Message.error, message="Expected " + str(size) + " '" + char + "' but got " + str(count));


def protocol_errors(testcase, messages):
    for message in messages:
        if message.severity == Severity.FAILURE:
            testcase.severity = Severity.FAILURE
        yield message


def run():
    checker = Checker()
    with checker.context():
        checker.initialize()

        if os.path.exists("sandbox"):
            # timeouts of previous checkers might leave a sandbox directory here,
            # so we don't raise an exception but simply delete it
            # bad luck if there's parts of a student's submission in there...
            shutil.rmtree("sandbox", ignore_errors=True)

        with contexts.NamedTempDir("sandbox"):
            for size in range(1,11):
                testcase = Test(checker, str(size) + "-Damen Problem")
                with testcase.context():
                    testcase.initialize()

                    # run the submission
                    stdout,stderr,returncode,time = submission.execute_submission(checker.java, checker.main, str(size))

                    output.print_execution(checker.document, checker.main + " " + str(size), [], stdout, stderr)

                    if returncode != 0:
                            raise StudentFailure("Executing submission failed with exitcode " + str(returncode))

                    stripped = [line.strip() for line in stdout]

                    # decide which checks to run
                    if size == 2 or size == 3:
                        checks = [
                            lambda x: check_errors(x, "Keine Belegung gefunden")
                            ]
                    else:
                        # strip all whitespace from the output
                        stripped = [re.sub("\s", "", line) for line in stripped if not len(line) == 0]

                        checks = [
                            lambda x: check_row_count(x, size),
                            lambda x: check_column_count(x, size),
                            lambda x: check_characters(x, " *."),
                            lambda x: check_count(x, "*", size),
                            lambda x: check_attacks(x, size)
                        ]

                    # run the checks
                    for check in checks:
                        messages = check(stripped)
                        messages = protocol_errors(testcase, messages)
                        output.print_messages(checker.document, messages)
                        if not testcase.severity == Severity.SUCCESS:
                            break

    return checker.result
