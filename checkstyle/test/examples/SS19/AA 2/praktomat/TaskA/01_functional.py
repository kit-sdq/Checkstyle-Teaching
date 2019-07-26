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
        'name': 'place',
        'description': 'Tests if placing all stones works correctly.',
        'protocol': 'place.txt'
    },
    {
        'name': 'move x',
        'description': 'Tests if moving x works correctly.',
        'protocol': 'moveX.txt'
    },
    {
        'name': 'move a',
        'description': 'Tests if moving a works correctly.',
        'protocol': 'moveA.txt'
    },
    {
        'name': 'move s',
        'description': 'Tests if moving s works correctly.',
        'protocol': 'moveS.txt'
    },
    {
        'name': 'move e',
        'description': 'Tests if moving e works correctly.',
        'protocol': 'moveE.txt'
    },
    {
        'name': 'move i',
        'description': 'Tests if moving i works correctly.',
        'protocol': 'moveI.txt'
    },
    {
        'name': 'reset',
        'description': 'Tests if resetting game works correctly (also after pass-draw).',
        'protocol': 'reset.txt'
    },
    {
        'name': 'draw',
        'description': 'Tests if a draw works correctly.',
        'protocol': 'draw.txt'
    },
    {
        'name': 'red wins',
        'description': 'Tests if red winning works correctly.',
        'protocol': 'redWin.txt'
    },
    {
        'name': 'violet wins',
        'description': 'Tests if violet winning works correctly.',
        'protocol': 'violetWin.txt'
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/functional/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if the functionality is given according to the specifications without error handling.")
sys.exit(0 if success else 1)
