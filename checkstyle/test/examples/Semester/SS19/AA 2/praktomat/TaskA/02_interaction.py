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
        'name': 'win by opponent red',
        'description': 'Tests if red winning by opponent is possible.',
        'protocol': 'winbyopponentRed.txt'
    },
    {
        'name': 'win by opponent violet',
        'description': 'Tests if violet winning by opponent is possible.',
        'protocol': 'winbyopponentViolet.txt'
    },
    {
        'name': 'move i different path representation',
        'description': 'Tests if moving i with different path representation is possible.',
        'protocol': 'differentPathRepresention.txt'
    },
    {
        'name': 'move agent in hole',
        'description': 'Tests if moving an agent into a hole is possible.',
        'protocol': 'moveAgentInHole.txt'
    },
    {
        'name': 'split field remain connected',
        'description': 'Tests if splitting the field at a position when the field as a whole remains unsplitted is possible.',
        'protocol': 'splitFieldRemainConnected.txt'
    },
    {
        'name': 'tests an example game 1',
        'description': 'Tests an example game 1.',
        'protocol': 'game1.txt'
    },
    {
        'name': 'tests an example game 2',
        'description': 'Tests an example game 2.',
        'protocol': 'game2.txt'
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/interaction/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test more complex interactions and whole games.")
sys.exit(0 if success else 1)
