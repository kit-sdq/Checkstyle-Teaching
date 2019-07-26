import sys
import zipfile
import os
import re
import subprocess

from pythomat import analysers
from pythomat import contexts
from pythomat import ipo
from pythomat import output
from pythomat import ressources
from pythomat import submission

from pythomat.analysers import Annotation
from pythomat.checker import Checker
from pythomat.exc import StudentFailure
from pythomat.exc import ConfigurationError
from pythomat.severity import Severity
from pythomat.test import Test


class SatAnalyser(analysers.StreamAnalyser):
    def __init__(self):
        self.got_result = False

    def analyse_line(self, line):
        sl_line = str(line).strip().lower()

        if sl_line == "sat":
            self.got_result = True
        elif sl_line == "unsat":
            self.got_result = True
            line.annotate_after("Expected 'SAT' as result, not 'UNSAT'")

    def finish_stream(self, stream):
        if not self.got_result:
            stream.annotate("Expected 'SAT'")

        return None


class UnsatAnalyser(analysers.StreamAnalyser):
    def __init__(self):
        self.got_result = False

    def analyse_line(self, line):
        sl_line = str(line).strip().lower()

        if sl_line == "unsat":
            self.got_result = True
        elif sl_line == "sat":
            self.got_result = True
            line.annotate_after("Expected 'UNSAT' as result, not 'SAT'")

    def finish_stream(self, stream):
        if not self.got_result:
            stream.annotate("Expected 'UNSAT'")


def parse_literal(literal):
    if len(literal.strip()) == 0:
        raise StudentFailure("Literal expected, but found empty string")
    try:
        parsed_literal = int(literal)
    except:
        raise StudentFailure("Could not parse literal: " + literal)
    if parsed_literal == 0:
        raise StudentFailure("Invalid variable id: 0")
    return parsed_literal


def parse_clause(clause):
    if len(clause.strip()) == 0:
        return frozenset()
    else:
        literals = clause.split(",")
        return frozenset([parse_literal(l) for l in literals])


resolution_re = re.compile("\{(.*)\}\s*\*\s*\{(.*)\}\s*=\s*\{(.*)\}")


def clause_to_str(clause):
    return "{" + ",".join([str(lit) for lit in clause]) + "}" 


def extract_resolvent(line):
    m = resolution_re.match(line)
    if m == None:
        raise StudentFailure("Could not match resolution line. Wrong format?")
    return parse_clause(m.group(3))


def extract_resolvents(log):
    resolvents = {}
    bucket = 0
    for line in log:
        sl_line = line.strip().lower()
        if len(sl_line) == 0:
            continue
        elif sl_line.startswith("processing bucket "):
            m = bucket_re.match(line)
            bucket = int(m.group(1))
            resolvents[bucket] = set()
            
        elif sl_line.startswith("{"):
            resolvents[bucket].add(extract_resolvent(sl_line))
    
    return resolvents


bucket_re = re.compile("[pP]rocessing\s*[bB]ucket\s*([0-9]*)")

class ResolutionStepAnalyser(analysers.StreamAnalyser):
    def __init__(self, resolvents):
        self.resolvents = resolvents

    def start_stream(self, stream):
        self.bucket = 0
        self.block = []


    def check_resolution_line(self, line):
        m = resolution_re.match(line)
        if not m:
            raise StudentFailure("Unexpected input: " + line)

        clause1 = parse_clause(m.group(1))
        clause2 = parse_clause(m.group(2))
        resolvent = parse_clause(m.group(3))

        if not self.bucket in clause1 and not -self.bucket in clause1:
            raise StudentFailure("Clause 1 does not contain bucket variable")

        if not self.bucket in clause2 and not -self.bucket in clause2:
            raise StudentFailure("Clause 2 does not contain bucket variable")

        if self.bucket in resolvent or -self.bucket in resolvent:
            raise StudentFailure("Resolvent still contains bucket variable")

        if self.bucket in clause1 and not -self.bucket in clause2:
            if self.bucket in clause2 and not -self.bucket in clause1:
                raise StudentFailure("Resolution on bucket variable not possible")

        res = set(clause1 | clause2)
        res.remove(self.bucket)
        res.remove(-self.bucket)

        if not res == resolvent:
            raise StudentFailure("Invalid resolvent")
   

    def check_block(self, expected_resolvents):
        msgs = []

        student_resolvents = set()
        for line in self.block:
            try:
                self.check_resolution_line(line)
            except StudentFailure as e:
                msgs.append(str(e))

            student_resolvents.add(extract_resolvent(line))

        for resolvent in student_resolvents - expected_resolvents:
            msgs.append("Unexpected resolution step with resolvent " + clause_to_str(resolvent) + " (Check tautology and subsumption checks)")

        # don't check for missing resolution steps if this is the last bucket
        if not frozenset() in expected_resolvents:
            for resolvent in expected_resolvents - student_resolvents: 
                msgs.append("Missing resolution step with resolvent " + clause_to_str(resolvent))

        return msgs


    def analyse_line(self, line):
        """We only close buckets here, nothing else"""
        sl_line = str(line).strip().lower()

        # skip empty lines
        if len(sl_line) == 0:
            return

        # annotate before we start a new bucket
        if sl_line.startswith("processing bucket ") or sl_line == "sat" or sl_line == "unsat":
            # check if the bucket so far was as expected
            # TODO: crahes if self.resolvents[self.bucket] doesn't exist!
            # when does this happen and why?
            # TODO: fix the code below before using the resultion exercise again
            if (self.bucket != -1):
                if self.bucket in self.resolvents:
                    if len(self.resolvents[self.bucket]) != 0:
                        explanation = str(self.check_block(self.resolvents[self.bucket]))
                        line.annotate_before("Something unexpected happened: " + explanation, Severity.CRASH)

        # annotate after the line
        if sl_line.startswith("processing bucket"):
            expected_bucket = self.bucket + 1
            try:
                m = bucket_re.match(sl_line)
                new_bucket = int(m.group(1))
            except:
                line.annotate_after("Not a valid bucket id: " + m.group(1))
                return 

            if new_bucket != expected_bucket:
                line.annotate_after("Unexpected bucket id in: " + sl_line + " (expected " + str(expected_bucket) + ", but got " + str(new_bucket) + ")")
                return

            self.bucket = new_bucket
            self.block = []

        elif sl_line.startswith("{"):
            # TODO: check if line is in the block here, instead of at the end of the block
            self.block.append(sl_line)

        elif sl_line == "sat":
            # TODO: check if sat is the expected result
            pass

        elif sl_line == "unsat":
            # TODO: check if unsat is the expected result
            pass

        else:
            line.annotate_after("Unexpected Output Line: " + sl_line)
            return


    def finish_stream(self, stream):
        expected_num = max(self.resolvents.keys()) if len(self.resolvents) > 0 else 0
        if self.bucket != expected_num:
            stream.annotate("Wrong number of buckets (expected " + str(expected_num) + ", but got " + str(self.bucket) + ")")


def extract_testcases(group):
    """Extract the test cases from the zip file."""

    for ressource in ressources.list(group):
        if ressource.endswith(".cnf"):
            testcase = {}

            basename = os.path.basename(ressource)
            testcase['arguments'] = basename

            cnf = ressources.read(ressource)
            testcase['files'] = { basename : cnf }

            log = ressources.read(os.path.splitext(ressource)[0] + ".log").splitlines()

            if basename.startswith("error_"):
                # this is a testcase requiring an error line
                stdout_analyser = analysers.ErrorAnalyser()
                testcase['analysers'] = analysers.error_analysers()
            else:
                resolvents_per_block = extract_resolvents(log)
                stdout_analyser = ResolutionStepAnalyser(resolvents_per_block)

                if any([line.strip().lower() == "sat" for line in log]):
                    stdout_analyser = analysers.ParallelAnalyser([stdout_analyser, SatAnalyser()])
                elif any([line.strip().lower() == "unsat" for line in log]):
                    stdout_analyser = analysers.ParallelAnalyser([stdout_analyser, UnsatAnalyser()])
                else:
                    raise ConfigurationError("neither SAT nor UNSAT line found in log")

                testcase['analysers'] = { 'stdout' : stdout_analyser }

            yield testcase


def run(files, group):

    configuration = extract_testcases(group)
    ipo.run(files, configuration)
