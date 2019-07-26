import cgi
import itertools
import json
import os.path
import re
import subprocess
import sys
import traceback
import zipfile

from pythomat import contexts
from pythomat import css
from pythomat import html
from pythomat import ipo
from pythomat import output
from pythomat import submission

from pythomat.checker import Checker
from pythomat.exc import StudentFailure
from pythomat.exc import ConfigurationError
from pythomat.test import Test


def split_nodes(string):
    return [int(tok) for tok in re.sub("\s", "", string).split(",")]


def add_costs(nodes, weight_map):
    edges = zip(nodes, nodes[1:])
    return sum([weight_map[edge] for edge in edges])


def create_weight_map(lines):
    weight_map = {}
    for line in lines:
        tokens = [int(tok) for tok in line.split(",")]
        weight_map[(tokens[0], tokens[1])] = tokens[2]
        weight_map[(tokens[1], tokens[0])] = tokens[2]
    return weight_map


def is_closed(nodes):
    return nodes[0] == nodes[-1]


def equal_sets(nodes_student, nodes):
    return set(nodes[1:]) == set(nodes_student[1:])


def analyze_regular_output(document, weights, expected_lines, output_lines):
    '''Check if the generated output is as expected'''

    expected_nodes = split_nodes(expected_lines[0])
    try:
        student_nodes = split_nodes(output_lines[0])
    except Exception as e:
        raise StudentFailure(str(e))

    weight_map = create_weight_map(weights)
    expected_cost = add_costs(expected_nodes, weight_map)
    student_cost = add_costs(student_nodes, weight_map)


    if (not is_closed(student_nodes)):
        raise StudentFailure("First and last node must be equal")

    if (not equal_sets(student_nodes, expected_nodes)):
        raise StudentFailure("Solution is not a roundtrip")

    if (student_cost != expected_cost):
        raise StudentFailure("Unexpected Total Weight")


def analyze_error_message(document, expected_message, output_lines):
    '''Check if the returned error message is as expected'''
    if not expected_message.match(output_lines[0].strip()):
        raise StudentFailure("Not the expected output")


def run_single_test(checker, test_json):
    '''Run a single test described in the string test_description'''

    input_content = test_json["input"]
    input_filename = temporary_input_file(input_content)

    output.print_file(checker.document, input_filename, input_content)

    stdin = []
    stdout,stderr,returncode,time = submission.execute_submission(checker.java, checker.main, input_filename, stdin)
    output.print_execution(checker.document, "java " + checker.main + " " + input_filename, stdin, stdout, stderr)

    if "expect" in test_json:
        # run a regular 'diff' test on the output
        expected_lines = test_json["expect"]
        if isinstance(expected_lines, basestring):
            expected_lines = [ expected_lines ]
        analyze_regular_output(checker.document, input_content, expected_lines, stdout)
    elif "error" in test_json:
        # check for an error message, but be relaxed about it
        error_message = test_json["error"]
        analyze_error_message(checker.document, re.compile(error_message), stdout)
    else:
        raise ConfigurationError("Cannot run test: Expected behaviour is unspecified.")


def extract_testcases(zip_filename):
    """Extract the test cases from the zip file."""

    if not zipfile.is_zipfile(zip_filename):
        raise ConfigurationError(zip_filename + " is not a valid zip file")

    zip_file = zipfile.ZipFile(zip_filename)
    test_files = zip_file.namelist()

    for test_file in test_files:
        # skip anything not ending in '.test'
        if os.path.splitext(test_file)[1] != ".test":
            continue

        test_json = json.loads(unicode(zip_file.read(test_file)))

        if not "name" in test_json:
            test_json["name"] = test_file

        yield test_json


def temporary_input_file(content):
    """Create a temporary input file in data and return it"""
    # TODO: try to clean up 'data', if we created it ourselves.
    filename = "data/test_input.txt"
    if not os.path.exists("data"):
        os.mkdir("data")
    with open(filename, 'w') as input_file:
        for line in content:
            input_file.write(line + '\n')
    return filename


def run_all_tests(checker, tests):
    '''Run all tests stored in a zip file'''
    for test in tests:
        testcase = Test(checker, test["name"])
        with testcase.context():
            testcase.initialize()

            run_single_test(checker, test)


def run(files, descriptions):

    return ipo.run(files, descriptions)
