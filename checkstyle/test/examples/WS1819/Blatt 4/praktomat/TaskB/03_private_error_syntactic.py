#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'place no arguments',
        'description': 'Tests if the place command without arguments is handled correctly.',
        'protocol': 'placenoargs.txt',
    },
    {
        'name': 'place wrong number of arguments',
        'description': 'Tests if the place command with a wrong number of arguments is handled correctly.',
        'protocol': 'placewrongargsnum.txt',
    },
    {
        'name': 'place illegal arguments',
        'description': 'Tests if the place command with illegal arguments is handled correctly.',
        'protocol': 'placeillegalargs.txt',
    },
    {
        'name': 'turn no arguments',
        'description': 'Tests if the turn commands without arguments is handled correctly.',
        'protocol': 'turnnoargs.txt',
    },
    {
        'name': 'turn wrong number of arguments',
        'description': 'Tests if the turn commands with a wrong number of arguments is handled correctly.',
        'protocol': 'turnwrongargsnum.txt',
    },
    {
        'name': 'turn illegal arguments',
        'description': 'Tests if the turn commands with illegal arguments is handled correctly.',
        'protocol': 'turnillegalargs.txt',
    },
    {
        'name': 'print with arguments',
        'description': 'Tests if the print command with arguments is handled correctly.',
        'protocol': 'printargs.txt',
    },
    {
        'name': 'state no arguments',
        'description': 'Tests if the state command without arguments is handled correctly.',
        'protocol': 'statenoargs.txt',
    },
    {
        'name': 'state wrong number of arguments',
        'description': 'Tests if the state command with a wrong number of arguments is handled correctly.',
        'protocol': 'statewrongargsnum.txt',
    },
    {
        'name': 'state illegal arguments',
        'description': 'Tests if the state command with illegal arguments is handled correctly.',
        'protocol': 'stateillegalargs.txt',
    },
    {
        'name': 'token no arguments',
        'description': 'Tests if the token command without arguments is handled correctly.',
        'protocol': 'tokennoargs.txt',
    },
    {
        'name': 'token wrong number of arguments',
        'description': 'Tests if the token command with a wrong number of arguments is handled correctly.',
        'protocol': 'tokenwrongargsnum.txt',
    },
    {
        'name': 'token illegal arguments',
        'description': 'Tests if the token command with illegal arguments is handled correctly.',
        'protocol': 'tokenillegalargs.txt',
    },
    {
        'name': 'quit with arguments',
        'description': 'Tests if the quit command with arguments is handled correctly.',
        'protocol': 'quitargs.txt',
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/errsyntactic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="Tests if syntactical errors are correctly handled.")
sys.exit(0 if success else 1)
