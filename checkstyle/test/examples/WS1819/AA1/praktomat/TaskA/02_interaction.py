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
        'name': 'reset and quit in phase 2',
        'description': 'Tests if the reset and quit commands work in phase 2.',
        'protocol': 'resetAndQuitInPhase2.txt'
    },
    {
        'name': 'state',
        'description': 'Tests if the state command works correctly (an interaction with state).',
        'protocol': 'state.txt'
    },
    {
        'name': 'interaction and print',
        'description': 'Tests some interaction and if the print command works correctly.',
        'protocol': 'printandinteraction.txt'
    },
    {
        'name': 'move',
        'description': 'Tests if the move command works correctly (interactions with move).',
        'protocol': 'move.txt'
    },
    {
        'name': 'place inverted',
        'description': 'Tests if the place command with other input sorting works correctly.',
        'protocol': 'placeInverted.txt'
    },
    {
        'name': 'roll not equal insert move',
        'description': 'Tests if moving as far as the inserted stone is possible if roll result was lower.',
        'protocol': 'rollNotEqualInsertMove.txt'
    }
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/interaction/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if all other commands and interactions work correctly.")
sys.exit(0 if success else 1)
