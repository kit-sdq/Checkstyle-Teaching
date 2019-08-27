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
        'name': 'Variable command with recursion',
        'description': 'Tests if the variable command works.',
        'protocol': 'V_recursive.txt',
    },
	{
        'name': 'Multiple additions',
        'description': 'Tests if the variable command works.',
        'protocol': 'V_multipleAdditions.txt',
    },
	{
        'name': 'Multiple Subtractions',
        'description': 'Tests if the variable command works.',
        'protocol': 'V_multipleSubtractions.txt',
    },
	{
        'name': 'Multiple Multiplications',
        'description': 'Tests if the variable command works.',
        'protocol': 'V_multipleMults.txt',
    },
	{
        'name': 'Multiple Divisions',
        'description': 'Tests if the variable command works.',
        'protocol': 'V_multipleDivs.txt',
    },
	{
        'name': 'Check precedence',
        'description': 'Tests if the precedence is implemented correctly.',
        'protocol': 'V_checkPrecedence.txt',
    },
	{
        'name': 'Check if dividing zero works',
        'description': 'Tests if dividing zero is implemented correctly.',
        'protocol': 'V_divideZero.txt',
    },
	{
        'name': 'Check brackets preserving the order',
        'description': 'Tests if the brackets are implemented correctly.',
        'protocol': 'V_bracketsSimple.txt',
    },
	{
        'name': 'Check brackets changing the order',
        'description': 'Tests if the brackets are implemented correctly.',
        'protocol': 'V_bracketsChangeOrder.txt',
    },
	{
        'name': 'Check many brackets work',
        'description': 'Tests if the brackets are implemented correctly.',
        'protocol': 'V_manyBrackets.txt',
    },
	{
        'name': 'Check variable definition with other variables',
        'description': 'Tests if variable definitions are implemented correctly.',
        'protocol': 'V_simple.txt',
    },
	

]

for test in tests:
    test['protocol'] = 'copy/protocols/private/functional/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if the functionality is given according to the specifications without error handling.")
sys.exit(0 if success else 1)
