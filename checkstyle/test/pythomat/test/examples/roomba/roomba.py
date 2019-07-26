import os

from pythomat import ressources
from pythomat import ipo

from pythomat.analysers import StreamAnalyser
from pythomat.checker import Checker
from pythomat.severity import Severity


def arrowize(string):
    """Convert direction strings to unicode arrow characters"""
    return string.replace("left", "↶").replace("right", "↷").replace("forward", "↑").replace("backward", "↓")


class RoombaAnalyser(StreamAnalyser):
    def __init__(self):
        self.trainer = []

    def start_stream(self, stream):
        pass

    def analyse_line(self, anno_line):
        line = str(anno_line)
        if len(line) == 0:
            return

        if line.startswith("!?$%&/()="):
            anno_line.hide = True
            self.trainer.append(arrowize(line[9:]))

    def finish_stream(self, stream):
        stream.annotate(self.trainer, Severity.INFO)


def list_testcases(group, exit_on_error):
    """Extract the test cases from the zip file."""

    for ressource in ressources.list(group):
        testcase = {}

        basename = os.path.basename(ressource)
        testcase['name'] = basename

        if exit_on_error:
            arguments = basename + " 1000"
        else:
            arguments= basename + " 1000 false"
        testcase['arguments'] = arguments

        testcase['files'] = { basename : ressources.read(ressource) }
        testcase['analysers'] = { 'stdout' : RoombaAnalyser() }

        yield testcase

def run(files, group, exit_on_error = True):

    descriptions = list_testcases(group, exit_on_error)
    return ipo.run(files, descriptions)
