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
        'name': 'state out of bounds',
        'description': 'Tests if using the state command on an out of bounds coordinate outputs an error message.',
        'protocol': 'stateOutOfBounds.txt'
    },
    {
        'name': 'set-vc out of bounds',
        'description': 'Tests if using the set-vc command on an out of bounds coordinate outputs an error message.',
        'protocol': 'setvcOutOfBounds.txt'
    },
    {
        'name': 'roll wrong',
        'description': 'Tests if rolling a nonexisting dice-side outputs an error message.',
        'protocol': 'rollWrong.txt'
    },
    {
        'name': 'place out of bounds',
        'description': 'Tests if using the place command on out of bounds coordinates outputs an error message.',
        'protocol': 'placeOutOfBounds.txt'
    },
    {
        'name': 'place overlap',
        'description': 'Tests if placing two stones overlapping outputs an error message.',
        'protocol': 'placeOverlap.txt'
    },
    {
        'name': 'place wrong size',
        'description': 'Tests if placing a stone of a wrong size outputs an error message.',
        'protocol': 'placeWrongSize.txt'
    },
    {
        'name': 'move out of bounds',
        'description': 'Tests if using the place command on out of bounds coordinates outputs an error message.',
        'protocol': 'moveOutOfBounds.txt'
    },
    {
        'name': 'move overlap',
        'description': 'Tests if moving over another occupied field outputs an error message.',
        'protocol': 'moveOverlap.txt'
    },
    {
        'name': 'move wrong size',
        'description': 'Tests if moving a wrong length outputs an error message.',
        'protocol': 'moveWrongSize.txt'
    },
    {
        'name': 'move wrong way',
        'description': 'Tests if moving a wrong path outputs an error message.',
        'protocol': 'moveWrongWay.txt'
    },
    {
        'name': 'place and move wrong phase',
        'description': 'Tests if placing and moving before rolling outputs an error message.',
        'protocol': 'placeAndMoveWrongPhase.txt'
    },
    {
        'name': 'set-vc and show-result wrong phase',
        'description': 'Tests if set-vc and show-result in wrong phase outputs an error message.',
        'protocol': 'setvcAndResultWrongPhase.txt'
    }
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/errsemantic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="Tests if semantical errors are correctly handled.")
sys.exit(0 if success else 1)
