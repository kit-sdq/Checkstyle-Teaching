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
        'name': 'interaction test 1',
        'description': 'Tests if an unbalanced tree works.',
        'protocol': 'interactionTest1.txt',
    },
	{
        'name': 'interaction test 2',
        'description': 'Tests if a tree with one child for every level works.',
        'protocol': 'interactionTest2.txt',
    },
	{
        'name': 'interaction test 3',
        'description': 'Tests if a tree with loops works.',
        'protocol': 'interactionTest3.txt',
    },
	{
        'name': 'interaction test 4',
        'description': 'Tests if another tree with loops works.',
        'protocol': 'interactionTest4.txt',
    },
	{
        'name': 'interaction test 5',
        'description': 'Tests if a balanced tree works.',
        'protocol': 'interactionTest5.txt',
    },
	{
        'name': 'interaction test 6',
        'description': 'Tests if another tree works.',
        'protocol': 'interactionTest6.txt',
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/interaction/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="Tests interactions.")
sys.exit(0 if success else 1)
