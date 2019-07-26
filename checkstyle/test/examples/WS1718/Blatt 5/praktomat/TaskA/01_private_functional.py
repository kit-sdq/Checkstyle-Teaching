#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'Win vertically P1',
        'description': 'Tests if winning vertically as player 1 works.',
        'protocol': 'p1vertical.txt',
    },
    {
        'name': 'Win diagonally P1',
        'description': 'Tests if winning diagonally as player 1 works.',
        'protocol': 'p1diagonal.txt',
    },
    {
        'name': 'Win antidiagonally P1',
        'description': 'Tests if winning antidiagonally as player 1 works.',
        'protocol': 'p1antidiagonal.txt',
    },
    {
        'name': 'Win horizontally P2',
        'description': 'Tests if winning horizontally as player 2 works.',
        'protocol': 'p2horizontal.txt',
    },
    {
        'name': 'Win vertically P2',
        'description': 'Tests if winning vertically as player 2 works.',
        'protocol': 'p2vertical.txt',
    },
    {
        'name': 'Win diagonally P2',
        'description': 'Tests if winning diagonally as player 2 works.',
        'protocol': 'p2diagonal.txt',
    },
    {
        'name': 'Win antidiagonally P2',
        'description': 'Tests if winning antidiagonally as player 2 works.',
        'protocol': 'p2antidiagonal.txt',
    },
    {
        'name': 'Draw',
        'description': 'Tests if drawing the game works.',
        'protocol': 'draw.txt',
    },
    {
        'name': 'Win at edge',
        'description': 'Tests if winning at the edge of the board works and doesn\'t crash the game.',
        'protocol': 'edge.txt',
    },
    {
        'name': 'print after draw',
        'description': 'Tests if printing a full board works correctly.',
        'protocol': 'printdraw.txt',
    },
    {
        'name': 'state',
        'description': 'Tests if the state command works correctly.',
        'protocol': 'state.txt',
    },
    {
        'name': 'state after game over',
        'description': 'Tests if the state command works correctly after the game is over.',
        'protocol': 'stategameover.txt',
    },
    {
        'name': 'interactive',
        'description': 'Tests a long interactive sequence works without commands influencing each other.',
        'protocol': 'interactive.txt',
    }
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/functional/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if the game in general works correctly with correct input.")
sys.exit(0 if success else 1)
