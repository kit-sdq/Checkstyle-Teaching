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
        'name': 'Wrong number of brackets',
        'description': 'Tests if wrong brackets result in an error.',
        'protocol': 'V_wrongNumberOfBrackets.txt',
    },
	{
        'name': 'Wrong whitespaces',
        'description': 'Tests if wrong whitespaces result in an error.',
        'protocol': 'V_wrongWhitespaces.txt',
    },
	{
        'name': 'Doubles',
        'description': 'Tests if doubles do not work.',
        'protocol': 'V_doubles.txt',
    },
	{
        'name': 'Reserved keywords',
        'description': 'Tests if java names result in an error.',
        'protocol': 'V_javaNames.txt',
    },
	{
        'name': 'Assignment invalid',
        'description': 'Tests if variable values are unchanged if the assignment is invalid.',
        'protocol': 'V_declInvalid.txt',
    },
	{
        'name': 'Variable not defined',
        'description': 'Tests if not defined variables result in an error.',
        'protocol': 'V_variableNotDefined.txt',
    },
	
	
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/error_semantic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if logical and semantic errors are handled correctly.")
sys.exit(0 if success else 1)
