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
        'name': 'move before X',
        'description': 'Tests if moving before Mr. X is placed causes an error.',
        'protocol': 'moveBeforeX.txt',
    },
    {
        'name': 'place next to opponent',
        'description': 'Tests if place next to opponent tile causes an error.',
        'protocol': 'placeOpponent.txt',
    },
    {
        'name': 'illegal state command use',
        'description': 'Tests if using commands when game is not active causes an error.',
        'protocol': 'illegalState.txt',
    },
    {
        'name': 'slide not possible',
        'description': 'Tests if a move where sliding is not possible causes an error.',
        'protocol': 'slideNotPossible.txt',
    },
    {
        'name': 'split',
        'description': 'Tests if splitting the field causes an error.',
        'protocol': 'split.txt',
    },
    {
        'name': 'split temp',
        'description': 'Tests if temporarily splitting the field causes an error.',
        'protocol': 'splitTmp.txt',
    },
    {
        'name': 'illegal path x',
        'description': 'Tests if an illegal move path for x causes an error.',
        'protocol': 'illegalPathX.txt',
    },
    {
        'name': 'illegal path a',
        'description': 'Tests if an illegal move path for a causes an error.',
        'protocol': 'illegalPathA.txt',
    },
    {
        'name': 'illegal path s',
        'description': 'Tests if an illegal move path for s causes an error.',
        'protocol': 'illegalPathS.txt',
    },
    {
        'name': 'illegal path e',
        'description': 'Tests if an illegal move path for e causes an error.',
        'protocol': 'illegalPathE.txt',
    },
    {
        'name': 'illegal path i',
        'description': 'Tests if an illegal move path for i causes an error.',
        'protocol': 'illegalPathI.txt',
    },
    {
        'name': 'move non exisiting',
        'description': 'Tests if moving a not-placed stone causes an error.',
        'protocol': 'moveNonExisting.txt',
    },
    {
        'name': 'place already placed',
        'description': 'Tests if placing a stone twice causes an error.',
        'protocol': 'placePlaced.txt',
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/error_semantic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if logical and semantic errors are handled correctly.")
sys.exit(0 if success else 1)
