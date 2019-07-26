#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'Win horizontally 2 players across borders',
        'description': 'Tests if winning horizontally in a game with 2 players across borders works.',
        'protocol': 'hor2.txt',
        'arguments': 'torus 18 2'
    },
    {
        'name': 'Win vertically 2 players across borders',
        'description': 'Tests if winning vertically in a game with 2 players across borders works.',
        'protocol': 'vert2.txt',
        'arguments': 'torus 18 2'
    },
    {
        'name': 'Win main diagonal',
        'description': 'Tests if winning on the main diagonal across borders works.',
        'protocol': 'maindiag2.txt',
        'arguments': 'torus 18 2'
    },
    {
        'name': 'Win main antidiagonal',
        'description': 'Tests if winning on the main antidiagonal across borders works.',
        'protocol': 'mainadiag2.txt',
        'arguments': 'torus 18 2'
    },
    {
        'name': 'Win diagonally 2 players across borders',
        'description': 'Tests if winning diagonally in a game with 2 players across borders works.',
        'protocol': 'diag2.txt',
        'arguments': 'torus 18 2'
    },
    {
        'name': 'Win antidiagonally 2 players across borders',
        'description': 'Tests if winning antidiagonally in a game with 2 players across borders works.',
        'protocol': 'adiag2.txt',
        'arguments': 'torus 18 2'
    },
    {
        'name': 'Win horizontally 2 players across borders bigger board',
        'description': 'Tests if winning horizontally in a game with 2 players across borders works on a 20x20 board.',
        'protocol': 'horbig2.txt',
        'arguments': 'torus 20 2'
    },
    {
        'name': '18x18 board win doesn\'t work on 20x20 board',
        'description': 'Tests if a 18x18 win doesn\'t work on a 20x20 board as intended.',
        'protocol': '18on20.txt',
        'arguments': 'torus 20 2'
    },
    {
        'name': 'Win vertically with outside placement',
        'description': 'Tests if the vertical testcase win still works when placing on fields outside of the board.',
        'protocol': 'vert2outside.txt',
        'arguments': 'torus 18 2'
    },
    {
        'name': 'Draw 2 players',
        'description': 'Tests if drawing the game with 2 players works.',
        'protocol': 'draw2.txt',
        'arguments': 'torus 18 2'
    },
    {
        'name': 'Draw 3 players',
        'description': 'Tests if drawing the game with 3 players works.',
        'protocol': 'draw3.txt',
        'arguments': 'torus 18 3'
    },
    {
        'name': 'Draw 4 players',
        'description': 'Tests if drawing the game with 4 players works.',
        'protocol': 'draw4.txt',
        'arguments': 'torus 18 4'
    },
    {
        'name': 'Draw 2 players bigger board',
        'description': 'Tests if drawing the game with 2 players works on a 20x20 board.',
        'protocol': 'drawbig2.txt',
        'arguments': 'torus 20 2'
    },
    {
        'name': 'Win at edge',
        'description': 'Tests if winning at the edge of the board works and doesn\'t crash the game.',
        'protocol': 'edge.txt',
        'arguments': 'torus 18 2'
    },
    {
        'name': 'Win horizontally 3 players',
        'description': 'Tests if winning horizontally in a game with 3 players still works in torus mode.',
        'protocol': 'horstandard3.txt',
        'arguments': 'torus 18 3'
    },
    {
        'name': 'Win vertically 2 players',
        'description': 'Tests if winning vertically in a game with 2 players still works in torus mode.',
        'protocol': 'vertstandard2.txt',
        'arguments': 'torus 18 2'
    },
    {
        'name': 'Win diagonally 4 players',
        'description': 'Tests if winning diagonally in a game with 4 players still works in torus mode.',
        'protocol': 'diagstandard4.txt',
        'arguments': 'torus 18 4'
    },
    {
        'name': 'Win antidiagonally 2 players bigger board',
        'description': 'Tests if winning antidiagonally in a game with 2 players still works on a 20x20 board.',
        'protocol': 'adiagstandardbig2.txt',
        'arguments': 'torus 20 2'
    },
    {
        'name': 'Visual diagonal across border',
        'description': 'Tests if a visual diagonal across borders isn\'t a win in torus mode as intended.',
        'protocol': 'visualdiag.txt',
        'arguments': 'torus 18 2'
    },
    {
        'name': 'Visual antidiagonal across border',
        'description': 'Tests if a visual antidiagonal across borders isn\'t a win in torus mode as intended.',
        'protocol': 'visualadiag.txt',
        'arguments': 'torus 18 2'
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/logictorus/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if the game logic works in torus mode.")
sys.exit(0 if success else 1)
