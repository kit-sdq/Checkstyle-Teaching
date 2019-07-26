#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': "n negative",
        'description': "Checks if the program handles a negative n correctly.",
        'arguments': "-5 1 1"
    },
    {
        'name': "n invalid",
        'description': "Checks if the program handles an invalid n correctly.",
        'arguments': "a 1 1"
    },
    {
        'name': "n float",
        'description': "Checks if the program handles a float for n correctly.",
        'arguments': "5.0 1 1"
    },
    {
        'name': "a invalid",
        'description': "Checks if the program handles an invalid a correctly.",
        'arguments': "5 x 1"
    },
    {
        'name': "a float",
        'description': "Checks if the program handles a float for a correctly.",
        'arguments': "5 1.0 1"
    },
    {
        'name': "b invalid",
        'description': "Checks if the program handles an invalid b correctly.",
        'arguments': "5 1 f"
    },
    {
        'name': "b float",
        'description': "Checks if the program handles a float for b correctly.",
        'arguments': "5 1 1.0"
    },
    {
        'name': "less than 3 parameters",
        'description': "Checks if the program handles less than 3 parameters correctly.",
        'arguments': "5 1"
    },
    {
        'name': "more than 3 parameters",
        'description': "Checks if the program handles more than 3 parameters correctly.",
        'arguments': "5 1 1 1"
    },
    {
        'name': "no parameters",
        'description': "Checks if the program handles no given parameters correctly.",
        'arguments': ""
    }
]

for test in tests:
    test['analysers'] = {
        'stdout': analysers.ExceptionAnalyser(analysers.ErrorAnalyser()),
        'stderr': analysers.ExceptionAnalyser()
    }

success = ipo.run(sys.argv[1:], tests,
                  description="These are tests to check the error handling when input is incorrect.")
sys.exit(0 if success else 1)
