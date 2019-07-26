#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'Invalid command line arguments - missing coordinates',
        'description': 'Tests if the command line parsing is working.',
        'protocol': 'error.txt',
		'arguments': '5;red;1;1;blue;;4;yellow;2;3;blue;2;2',
    },
    {
        'name': 'Invalid command line arguments - missing places',
        'description': 'Tests if the command line parsing is working.',
        'protocol': 'error.txt',
		'arguments': '5;red;blue;yellow;blue',
    },
    {
        'name': 'Valid but uncommon command line arguments - special characters',
        'description': 'Tests if the command line parsing is working.',
        'protocol': 'quit.txt',
		'arguments': '5;red.;1;1;bl,ue;4;4;yel/low;2;3;bl§ue;2;2',
    },
    {
        'name': 'Valid but uncommon command line arguments - numbers',
        'description': 'Tests if the command line parsing is working.',
        'protocol': 'quit.txt',
		'arguments': '5;red2;1;1;blu3e;4;4;yell4ow;2;3;bl5ue;2;2',
    },
    {
        'name': 'Commands without parameters',
        'description': 'Tests if the command line parsing is working and commands without parameters result in an error.',
        'protocol': 'commandsNoParam.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;blue;2;2',
    },
    {
        'name': 'Commands with invalid parameters - double',
        'description': 'Tests if the command line parsing is working and commands with wrong parameters result in an error.',
        'protocol': 'paramsDouble.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;blue;2;2',
    },
    {
        'name': 'Commands with invalid parameters - letters',
        'description': 'Tests if the command line parsing is working and commands with wrong parameters result in an error.',
        'protocol': 'paramsLetters.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;blue;2;2',
    },
    {
        'name': 'Commands with invalid parameters - invalid figure name',
        'description': 'Tests if the command line parsing is working and commands with wrong parameters result in an error.',
        'protocol': 'invalidFigureName.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;blue;2;2',
    },
    {
        'name': 'Commands with invalid parameters - invalid building parameter',
        'description': 'Tests if the command line parsing is working and commands with wrong parameters result in an error.',
        'protocol': 'invalidCD.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;blue;2;2',
    },
    {
        'name': 'Commands with invalid parameters - negative coordinates',
        'description': 'Tests if the command line parsing is working and commands with wrong parameters result in an error.',
        'protocol': 'negCoord.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;blue;2;2',
    },

]

for test in tests:
    test['protocol'] = 'copy/protocols/private/error_syntactic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="Tests if syntactic errors are correctly handled.")
sys.exit(0 if success else 1)
