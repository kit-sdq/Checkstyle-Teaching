#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'place already occupied',
        'description': 'Tests if using the place command on an already occupied cell is handled correctly.',
        'protocol': 'placeoccupied.txt',
        'arguments': 'standard 18 2'
    },
    {
        'name': 'place error doesn\'t change player',
        'description': 'Tests if an erroneous place command doesn\'t change the player.',
        'protocol': 'placechange.txt',
        'arguments': 'standard 18 2'
    },
    {
        'name': 'place nonexistent coordinates',
        'description': 'Tests if using nonexistent coordinates in the place command is handled correctly.',
        'protocol': 'placenonexistent.txt',
        'arguments': 'standard 18 2'
    },
    {
        'name': 'place after win',
        'description': 'Tests if using the place command after the game is won isn\'t possible.',
        'protocol': 'placewon.txt',
        'arguments': 'standard 18 2'
    },
    {
        'name': 'place after draw',
        'description': 'Tests if using the place command after the game is drawn isn\'t possible.',
        'protocol': 'placedraw.txt',
        'arguments': 'standard 18 2'
    },
    {
        'name': 'rowprint nonexistent row',
        'description': 'Tests if using a nonexistent row in the rowprint command is handled correctly.',
        'protocol': 'rowprintnonexistent.txt',
        'arguments': 'standard 18 2'
    },
    {
        'name': 'rowprint nonexistent row torus',
        'description': 'Tests if using a nonexistent row in the rowprint command is handled correctly in torus mode.',
        'protocol': 'rowprintnonexistent.txt',
        'arguments': 'torus 18 2'
    },
    {
        'name': 'colprint nonexistent column',
        'description': 'Tests if using a nonexistent column in the colprint command is handled correctly.',
        'protocol': 'colprintnonexistent.txt',
        'arguments': 'standard 18 2'
    },
    {
        'name': 'colprint nonexistent column torus',
        'description': 'Tests if using a nonexistent column in the colprint command is handled correctly in torus mode.',
        'protocol': 'colprintnonexistent.txt',
        'arguments': 'torus 18 2'
    },
    {
        'name': 'state nonexistent coordinates',
        'description': 'Tests if using nonexistent coordinates in the state command is handled correctly.',
        'protocol': 'statenonexistent.txt',
        'arguments': 'standard 18 2'
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/errsemantic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="Tests if semantical errors are correctly handled.")
sys.exit(0 if success else 1)
