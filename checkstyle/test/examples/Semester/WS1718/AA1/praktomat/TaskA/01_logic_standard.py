#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'Win vertically 2 players',
        'description': 'Tests if winning vertically in a game with 2 players works.',
        'protocol': 'vert2.txt',
        'arguments': 'standard 18 2'
    },
    {
        'name': 'Win diagonally 2 players',
        'description': 'Tests if winning diagonally in a game with 2 players works.',
        'protocol': 'diag2.txt',
        'arguments': 'standard 18 2'
    },
    {
        'name': 'Win antidiagonally 2 players',
        'description': 'Tests if winning antidiagonally in a game with 2 players works.',
        'protocol': 'adiag2.txt',
        'arguments': 'standard 18 2'
    },
    {
        'name': 'Win horizontally 3 players',
        'description': 'Tests if winning horizontally in a game with 3 players works.',
        'protocol': 'hor3.txt',
        'arguments': 'standard 18 3'
    },
    {
        'name': 'Win vertically 3 players',
        'description': 'Tests if winning vertically in a game with 3 players works.',
        'protocol': 'vert3.txt',
        'arguments': 'standard 18 3'
    },
    {
        'name': 'Win diagonally 3 players',
        'description': 'Tests if winning diagonally in a game with 3 players works.',
        'protocol': 'diag3.txt',
        'arguments': 'standard 18 3'
    },
    {
        'name': 'Win antidiagonally 3 players',
        'description': 'Tests if winning antidiagonally in a game with 3 players works.',
        'protocol': 'adiag3.txt',
        'arguments': 'standard 18 3'
    },
    {
        'name': 'Win horizontally 4 players',
        'description': 'Tests if winning horizontally in a game with 4 players works.',
        'protocol': 'hor4.txt',
        'arguments': 'standard 18 4'
    },
    {
        'name': 'Win vertically 4 players',
        'description': 'Tests if winning vertically in a game with 4 players works.',
        'protocol': 'vert4.txt',
        'arguments': 'standard 18 4'
    },
    {
        'name': 'Win diagonally 4 players',
        'description': 'Tests if winning diagonally in a game with 4 players works.',
        'protocol': 'diag4.txt',
        'arguments': 'standard 18 4'
    },
    {
        'name': 'Win antidiagonally 4 players',
        'description': 'Tests if winning antidiagonally in a game with 4 players works.',
        'protocol': 'adiag4.txt',
        'arguments': 'standard 18 4'
    },
    {
        'name': 'Win horizontally 2 players bigger board',
        'description': 'Tests if winning horizontally in a game with 2 players works on a 20x20 board.',
        'protocol': 'horbig2.txt',
        'arguments': 'standard 20 2'
    },
    {
        'name': 'Win vertically 3 players bigger board',
        'description': 'Tests if winning vertically in a game with 3 players works on a 20x20 board.',
        'protocol': 'vertbig3.txt',
        'arguments': 'standard 20 3'
    },
    {
        'name': 'Win diagonally 4 players bigger board',
        'description': 'Tests if winning diagonally in a game with 4 players works on a 20x20 board.',
        'protocol': 'diagbig4.txt',
        'arguments': 'standard 20 4'
    },
    {
        'name': 'Win antidiagonally 2 players bigger board',
        'description': 'Tests if winning antidiagonally in a game with 2 players works on a 20x20 board.',
        'protocol': 'adiagbig2.txt',
        'arguments': 'standard 20 2'
    },
    {
        'name': 'Draw 2 players',
        'description': 'Tests if drawing the game with 2 players works.',
        'protocol': 'draw2.txt',
        'arguments': 'standard 18 2'
    },
    {
        'name': 'Draw 3 players',
        'description': 'Tests if drawing the game with 3 players works.',
        'protocol': 'draw3.txt',
        'arguments': 'standard 18 3'
    },
    {
        'name': 'Draw 4 players',
        'description': 'Tests if drawing the game with 4 players works.',
        'protocol': 'draw4.txt',
        'arguments': 'standard 18 4'
    },
    {
        'name': 'Draw 2 players bigger board',
        'description': 'Tests if drawing the game with 2 players works on a 20x20 board.',
        'protocol': 'drawbig2.txt',
        'arguments': 'standard 20 2'
    },
    {
        'name': 'Win at edge',
        'description': 'Tests if winning at the edge of the board works and doesn\'t crash the game.',
        'protocol': 'edge.txt',
        'arguments': 'standard 18 2'
    },
    {
        'name': 'Win across borders',
        'description': 'Tests if winning across borders doesn\'t work as intended in standard mode.',
        'protocol': 'border.txt',
        'arguments': 'standard 18 2'
    }
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/logicstandard/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if the game logic works in standard mode.")
sys.exit(0 if success else 1)
