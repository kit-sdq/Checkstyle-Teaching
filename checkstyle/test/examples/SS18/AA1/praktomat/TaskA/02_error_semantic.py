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
        'name': 'unknown game mode',
        'description': 'Tests if an unknown game mode is handled correctly.',
        'protocol': 'error.txt',
        'arguments': '3d 32',
    },
    {
        'name': 'token count too high',
        'description': 'Tests if a too high token count is handled correctly.',
        'protocol': 'error.txt',
        'arguments': 'standard 33',
    },
    {
        'name': 'token count too low',
        'description': 'Tests if a too low token count is handled correctly.',
        'protocol': 'error.txt',
        'arguments': 'standard 27',
    },
    {
        'name': 'token count negative',
        'description': 'Tests if a negative token count is handled correctly.',
        'protocol': 'error.txt',
        'arguments': 'standard -1337',
    },
    {
        'name': 'token count not a number',
        'description': 'Tests if a NaN token count is handled correctly.',
        'protocol': 'error.txt',
        'arguments': 'standard foo$bar',
    },
    {
        'name': 'throwin column out of range',
        'description': 'Tests if the throwin command with an out of range column number is handled correctly.',
        'protocol': 'throwincoloutofrange.txt',
        'arguments': 'standard 32',
    },
    {
        'name': 'error doesn\'t switch the player',
        'description': 'Tests if an error doesn\'t switch the active player.',
        'protocol': 'errorstayactive.txt',
        'arguments': 'standard 32',
    },
    {
        'name': 'throwin full column',
        'description': 'Tests if the throwin command throwing into a full column is handled correctly.',
        'protocol': 'throwinfull.txt',
        'arguments': 'standard 32',
    },
    {
        'name': 'throwin after win',
        'description': 'Tests if the throwin command after a win is handled correctly.',
        'protocol': 'throwinafterwin.txt',
        'arguments': 'standard 32',
    },
    {
        'name': 'throwin after draw ',
        'description': 'Tests if the throwin command after a draw is handled correctly.',
        'protocol': 'throwinafterdraw.txt',
        'arguments': 'standard 32',
    },
    {
        'name': 'flip after win',
        'description': 'Tests if the flip command after a win is handled correctly.',
        'protocol': 'flipafterwin.txt',
        'arguments': 'flip 32',
    },
    {
        'name': 'flip after draw',
        'description': 'Tests if the flip command after a draw is handled correctly.',
        'protocol': 'flipafterdraw.txt',
        'arguments': 'flip 32',
    },
    {
        'name': 'remove enemy token',
        'description': 'Tests if the remove command removing an enemy token is handled correctly.',
        'protocol': 'removeenemytoken.txt',
        'arguments': 'remove 32',
    },
    {
        'name': 'remove empty column',
        'description': 'Tests if the remove command removing from an empty column is handled correctly.',
        'protocol': 'removeemptycol.txt',
        'arguments': 'remove 32',
    },
    {
        'name': 'remove after win',
        'description': 'Tests if the remove command after a win is handled correctly.',
        'protocol': 'removeafterwin.txt',
        'arguments': 'remove 32',
    },
    {
        'name': 'remove after draw',
        'description': 'Tests if the remove command after a draw is handled correctly.',
        'protocol': 'removeafterdraw.txt',
        'arguments': 'remove 32',
    },
    {
        'name': 'flip/remove in standard mode',
        'description': 'Tests if using the flip or remove command in standard mode is handled correctly.',
        'protocol': 'flipremoveinstandard.txt',
        'arguments': 'standard 32',
    },
    {
        'name': 'remove in flip mode',
        'description': 'Tests if using the remove command in flip mode is handled correctly.',
        'protocol': 'removeinflip.txt',
        'arguments': 'flip 32',
    },
    {
        'name': 'flip in remove mode',
        'description': 'Tests if using the flip command in remove mode is handled correctly.',
        'protocol': 'flipinremove.txt',
        'arguments': 'remove 32',
    },
    {
        'name': 'state out of bounds',
        'description': 'Tests if using the state command with out of bounds coordinates is handled correctly.',
        'protocol': 'stateoutofbounds.txt',
        'arguments': 'standard 32',
    }
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/error_semantic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if logical and semantic errors are handled correctly.")
sys.exit(0 if success else 1)
