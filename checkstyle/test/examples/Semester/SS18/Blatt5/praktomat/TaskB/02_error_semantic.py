#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    #game errors
    {
        'name': 'Dome cannot be build on wrong level',
        'description': 'Tests if the command line parsing is working and building a dome on the wrong level returns an error.',
        'protocol': 'buildDomeWrongLevel.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Jump up more than 1 level',
        'description': 'Tests if the command line parsing is working and jumping higher than 1 level returns an error.',
        'protocol': 'jmpWrong.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Move out of the board',
        'description': 'Tests if the command line parsing is working and trying to move out of the board returns an error.',
        'protocol': 'moveOutOfBoard.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Jump on dome',
        'description': 'Tests if the command line parsing is working and jumping on a dome returns an error.',
        'protocol': 'jumpOnDome.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Build on a nonadjacent field',
        'description': 'Tests if the command line parsing is working and building on a nonadjacent field returns an error.',
        'protocol': 'buildNonAdjacentField.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Build out of the board',
        'description': 'Tests if the command line parsing is working and building outside the board returns an error.',
        'protocol': 'buildOutOfBoard.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Build without prior move',
        'description': 'Tests if the command line parsing is working build before move returns an error.',
        'protocol': 'buildWithoutMove.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Place on occupied field',
        'description': 'Tests if the command line parsing is working and placing on an occupied field returns an error.',
        'protocol': 'placeOnOccupiedField.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Turn withouth build',
        'description': 'Tests if the command line parsing is working and turn without a prior build returns an error.',
        'protocol': 'turnWithoutBuild.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Turn without move',
        'description': 'Tests if the command line parsing is working and turn without a prior move returns an error.',
        'protocol': 'turnWithoutMove.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Build on a dome',
        'description': 'Tests if the command line parsing is working and building on a dome returns an error.',
        'protocol': 'buildOnDome.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Build higher than 3 levels',
        'description': 'Tests if the command line parsing is working and building higher than 3 returns an error.',
        'protocol': 'buildHigher3.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Cellprint out of the board',
        'description': 'Tests if the command line parsing is working and trying to cellprint a field outside of the board returns an error.',
        'protocol': 'cellprintOutside.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Move on occupied field',
        'description': 'Tests if the command line parsing is working and moving on an occupied field returns an error.',
        'protocol': 'moveOnOccupied.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Move figure of wrong player',
        'description': 'Tests if the command line parsing is working and trying to move a figure from the wrong player returns an error.',
        'protocol': 'moveWrongFigure.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },









    # command line parameter errors
    {
        'name': 'Wrong field size - 4',
        'description': 'Tests if an invalid field size returns an error.',
        'protocol': 'error.txt',
		'arguments': '4;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Wrong field size - 0',
        'description': 'Tests if an invalid field size returns an error.',
        'protocol': 'error.txt',
		'arguments': '0;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Wrong field size - 9',
        'description': 'Tests if an invalid field size returns an error.',
        'protocol': 'error.txt',
		'arguments': '9;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Place figure outside board',
        'description': 'Tests if an invalid place of a figure returns an error.',
        'protocol': 'error.txt',
		'arguments': '5;red;1;1;blue;5;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Place figure outside board 2',
        'description': 'Tests if an invalid place of a figure returns an error.',
        'protocol': 'error.txt',
		'arguments': '8;red;1;1;blue;100;4;yellow;2;3;green;2;2',
    },
    {
        'name': 'Place figures on top of each other - same team',
        'description': 'Tests if you cannot place two figures on top of each other.',
        'protocol': 'error.txt',
		'arguments': '5;red;1;1;blue;1;1;yellow;2;3;green;2;2',
    },
    {
        'name': 'Place figures on top of each other - different teams',
        'description': 'Tests if you cannot place two figures on top of each other.',
        'protocol': 'error.txt',
		'arguments': '5;red;1;1;blue;2;2;yellow;1;1;green;2;2',
    },

]

for test in tests:
    test['protocol'] = 'copy/protocols/private/error_semantic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="Tests if semantical errors are correctly handled.")
sys.exit(0 if success else 1)
