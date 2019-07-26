#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import interactive

tests = [
    {
        'name': "Creation of assignment",
        'description': "Checks if creation of assignments works correctly.",
        'protocol': 'assignment.txt',
    },
    {
        'name': "Creation and listing of pupils",
        'description': "Checks if creation and listing of pupils works correctly.",
        'protocol': 'pupils.txt',
    },
    {
        'name': "Reselecting a teacher",
        'description': "Checks if reselecting a teacher works correctly.",
        'protocol': 'reselect.txt',
    },
    {
        'name': "Submitting and listing of solutions",
        'description': "Checks if creation and listing of solutions works correctly.",
        'protocol': 'solutions.txt',
    },
    {
        'name': "Reviewing and listing of results",
        'description': "Checks if reviewing and listing of results works correctly.",
        'protocol': 'results.txt',
    },
	{
        'name': "Overwrite review",
        'description': "Checks if overwriting review works correctly.",
        'protocol': 'rereview.txt',
    },
    {
        'name': "Summary of assignments",
        'description': "Checks if summary of assignments works correctly.",
        'protocol': 'summaryassignment.txt',
    },
    {
        'name': "Summary of teacher",
        'description': "Checks if summary of teacher works correctly.",
        'protocol': 'summaryteacher.txt',
    },
    {
        'name': "Reset",
        'description': "Checks if reset works correctly.",
        'protocol': 'reset.txt',
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/functional/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                  description="These are tests to check the functionality of the program with correct input.")
sys.exit(0 if success else 1)
