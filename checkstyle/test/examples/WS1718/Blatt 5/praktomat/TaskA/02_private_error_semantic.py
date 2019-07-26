#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'place error doesn\'t change player',
        'description': 'Tests if an erroneous place command doesn\'t change the player.',
        'protocol': 'placechange.txt',
    },
    {
        'name': 'place already occupied',
        'description': 'Tests if using the place command on an already occupied cell is handled correctly.',
        'protocol': 'placeoccupied.txt',
    },
    {
        'name': 'place nonexistant coordinates',
        'description': 'Tests if using nonexistant coordinates in the place command is handled correctly.',
        'protocol': 'placenonexistant.txt',
    },
    {
        'name': 'place after win',
        'description': 'Tests if using the place command after the game is won isn\'t possible.',
        'protocol': 'placewon.txt',
    },
    {
        'name': 'place after draw',
        'description': 'Tests if using the place command after the game is drawn isn\'t possible.',
        'protocol': 'placedraw.txt',
    },
    {
        'name': 'state nonexistant coordinates',
        'description': 'Tests if using nonexistant coordinates in the state command is handled correctly.',
        'protocol': 'statenonexistant.txt',
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/errsemantic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="Tests if semantical errors are correctly handled.")
sys.exit(0 if success else 1)
