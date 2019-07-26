#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'Command line and cellprint - Player 1',
        'description': 'Tests if the command line parsing and the cellprint command are working.',
        'protocol': 'clAndCellprint.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Command line, move, cellprint - Player 1',
        'description': 'Tests if the command line parsing and the cellprint and move command are working.',
        'protocol': 'cpmv.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Command line, move, build, cellprint - Player 1',
        'description': 'Tests if the command line parsing and the cellprint and move and build command are working.',
        'protocol': 'cpmvbl.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Command line, move, build, cellprint, turn - Player 1',
        'description': 'Tests if the command line parsing and the cellprint and move and build and turn command are working.',
        'protocol': 'cpmvbltn.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Command line and cellprint - Player 2',
        'description': 'Tests if the command line parsing and the cellprint command are working.',
        'protocol': 'clAndCellprint0.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Command line, move, cellprint - Player 2',
        'description': 'Tests if the command line parsing and the cellprint and move command are working.',
        'protocol': 'cpmv0.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Command line, move, build, cellprint - Player 2',
        'description': 'Tests if the command line parsing and the cellprint and move and build command are working.',
        'protocol': 'cpmvbl0.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Command line, move, build, cellprint, turn - Player 2',
        'description': 'Tests if the command line parsing and the cellprint and move and build and turn command are working.',
        'protocol': 'cpmvbltn0.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Command line, cellprint with two figures with same name',
        'description': 'Tests if the command line parsing and the cellprint command are working if both players have a figure with the same name.',
        'protocol': 'cp2fsn.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;blue;2;2',
    },
    {
        'name': 'Command line, all commands and building a dome',
        'description': 'Tests if the command line parsing and all commands are working with a dome.',
        'protocol': 'buildDome.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Command line, all commands and winning player 2',
        'description': 'Tests if the command line parsing and all commands are working with player 2 winning.',
        'protocol': 'winningPlayer2.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Command line, all commands and winning player 1',
        'description': 'Tests if the command line parsing and all commands are working with player 1 winning.',
        'protocol': 'winningPlayer1.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Command line, all commands with a more complex game',
        'description': 'Tests if the command line parsing and all commands are working with a more complex gaming.',
        'protocol': 'complexGame.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Command line, all commands with a more complex game',
        'description': 'Tests if the command line parsing and all commands are working with another more complex gaming.',
        'protocol': 'complexGame0.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Command line and quit',
        'description': 'Tests if the command line parsing and quit works.',
        'protocol': 'quitAsFirstCommand.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Command line, move, quit',
        'description': 'Tests if the command line parsing and move and quit works.',
        'protocol': 'quitAfterMove.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Command line, move, build, quit',
        'description': 'Tests if the command line parsing, build, move and quit work.',
        'protocol': 'quitAfterBuild.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Command line, move, build, turn, quit',
        'description': 'Tests if the command line parsing, build, move, turn and quit work.',
        'protocol': 'quitAfterTurn.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/functional/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if the program works correctly with correct input.")
sys.exit(0 if success else 1)
