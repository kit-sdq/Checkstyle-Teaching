#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': "Longer series of example",
        'description': "Checks if the example from the assignment sheet works correctly with a longer series.",
        'arguments': "30 1 1",
        "stdout": "1,1,1,2,2,3,4,5,7,9,12,16,21,28,37,49,65,86,114,151,200,265,351,465,616,816,1081,1432,1897,2513,3329"
    },
    {
        'name': "a and b > 1",
        'description': "Checks if the example from the assignment sheet works correctly with a and b greater than 1.",
        'arguments': "10 5 3",
        "stdout": "1,1,1,8,8,43,64,239,449,1387,2962"
    },
    {
        'name': "a and b < 1",
        'description': "Checks if the example from the assignment sheet works correctly with a and b smaller than 1.",
        'arguments': "10 -2 -6",
        "stdout": "1,1,1,-8,-8,10,64,28,-188,-440,208"
    },
    {
        'name': "a and b mixed",
        'description': "Checks if the example from the assignment sheet works correctly with a and b mixed positive and negative.",
        'arguments': "10 -2 4",
        "stdout": "1,1,1,2,2,0,4,8,-8,0,48"
    },
    {
        'name': "a and b mixed",
        'description': "Checks if the example from the assignment sheet works correctly with a and b mixed positive and negative.",
        'arguments': "10 -2 4",
        "stdout": "1,1,1,2,2,0,4,8,-8,0,48"
    },
    {
        'name': "a zero",
        'description': "Checks if the example from the assignment sheet works correctly with a as zero.",
        'arguments': "10 0 4",
        "stdout": "1,1,1,4,4,4,16,16,16,64,64"
    },
    {
        'name': "b zero",
        'description': "Checks if the example from the assignment sheet works correctly with b as zero.",
        'arguments': "10 4 0",
        "stdout": "1,1,1,4,4,16,16,64,64,256,256"
    },
    {
        'name': "both zero",
        'description': "Checks if the example from the assignment sheet works correctly with both values as zero.",
        'arguments': "10 0 0",
        "stdout": "1,1,1,0,0,0,0,0,0,0,0"
    },
    {
        'name': "n 0",
        'description': "Checks if the example from the assignment sheet works correctly with n as zero.",
        'arguments': "0 1 1",
        "stdout": "1"
    },
    {
        'name': "n 1",
        'description': "Checks if the example from the assignment sheet works correctly with n as one.",
        'arguments': "1 1 1",
        "stdout": "1,1"
    },
    {
        'name': "n 2",
        'description': "Checks if the example from the assignment sheet works correctly with n as two.",
        'arguments': "2 1 1",
        "stdout": "1,1,1"
    }
]

for test in tests:
    test['analysers'] = {
        'stdout': analysers.ExceptionAnalyser(analysers.LineByLineAnalyser(test["stdout"])),
        'stderr': analysers.ExceptionAnalyser()
    }

success = ipo.run(sys.argv[1:], tests,
                  description="These are tests to check the functionality of the program with correct input.")
sys.exit(0 if success else 1)
