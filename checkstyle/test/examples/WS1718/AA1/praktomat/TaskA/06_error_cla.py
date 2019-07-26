#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'First argument not torus or standard',
        'description': 'Tests if a wrong mode input is handled correctly.',
        'protocol': 'error.txt',
        'arguments': 'lol 18 2'
    },
    {
        'name': 'Board size too low',
        'description': 'Tests if a too small board is handled correctly.',
        'protocol': 'error.txt',
        'arguments': 'standard 16 2'
    },
    {
        'name': 'Board size too high',
        'description': 'Tests if a too big board is handled correctly.',
        'protocol': 'error.txt',
        'arguments': 'standard 22 2'
    },
    {
        'name': 'Board size in range but not even',
        'description': 'Tests if a a boardsize that is in range but not even is handled correctly.',
        'protocol': 'error.txt',
        'arguments': 'standard 19 2'
    },
    {
        'name': 'Negative board size',
        'description': 'Tests if a negative board size is handled correctly.',
        'protocol': 'error.txt',
        'arguments': 'standard -1 2'
    },
    {
        'name': 'Board NaN',
        'description': 'Tests if a board size that is not a number is handled correctly.',
        'protocol': 'error.txt',
        'arguments': 'standard a 2'
    },
    {
        'name': 'Too little players',
        'description': 'Tests if a too little players are handled correctly.',
        'protocol': 'error.txt',
        'arguments': 'standard 18 1'
    },
    {
        'name': 'Too many players',
        'description': 'Tests if a too many players are handled correctly.',
        'protocol': 'error.txt',
        'arguments': 'standard 18 5'
    },
    {
        'name': 'Negative number of players',
        'description': 'Tests if a a negative number of players are handled correctly.',
        'protocol': 'error.txt',
        'arguments': 'standard 18 -1'
    },
    {
        'name': 'Number of players NaN',
        'description': 'Tests if a too little players are handled correctly.',
        'protocol': 'error.txt',
        'arguments': 'standard 18 a'
    },
    {
        'name': 'No arguments',
        'description': 'Tests if no arguments are handled correctly.',
        'protocol': 'error.txt',
    },
    {
        'name': 'Too many arguments',
        'description': 'Tests if a too many arguments are handled correctly.',
        'protocol': 'error.txt',
        'arguments': 'standard 18 1 hehe lul 42'
    },
    {
        'name': 'Wrong argument order',
        'description': 'Tests if a wrong argument order is handled correctly.',
        'protocol': 'error.txt',
        'arguments': '1 torus 18'
    }
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/errcla/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="Tests if command line argument errors are correctly handled.")
sys.exit(0 if success else 1)
