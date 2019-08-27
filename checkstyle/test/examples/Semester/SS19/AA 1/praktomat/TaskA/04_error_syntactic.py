#!/usr/bin/env python
# -*- encoding: utf-8 -*-
import logging
import os

if not(os.environ.get('PYTHOMAT_TESTS_LOG')):
	log_file = os.path.join(os.path.dirname(os.path.abspath(__file__)), "tests.log")
	os.environ['PYTHOMAT_TESTS_LOG'] = log_file

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'Assignment without arguments',
        'description': 'Tests if wrong args result in an error.',
        'protocol': 'declnoargs.txt',
    },
	{
        'name': 'Assignment with wrong args',
        'description': 'Tests if wrong args result in an error.',
        'protocol': 'declwrongargs.txt',
    },
	{
        'name': 'Quit with wrong args',
        'description': 'Tests if wrong args result in an error.',
        'protocol': 'quitargs.txt',
    },
	{
        'name': 'Variable command without arguments',
        'description': 'Tests if wrong args result in an error.',
        'protocol': 'variablenoargs.txt',
    },
	{
        'name': 'Variable command with wrongs arguments',
        'description': 'Tests if wrong args result in an error.',
        'protocol': 'variablewrongargs.txt',
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/error_syntactic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if wrong input is handled correctly.")
sys.exit(0 if success else 1)
