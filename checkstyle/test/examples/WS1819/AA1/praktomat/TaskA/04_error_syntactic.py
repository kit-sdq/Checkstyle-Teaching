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
        'name': 'state no arguments',
        'description': 'Tests if the state command without arguments is handled correctly.',
        'protocol': 'statenoargs.txt'
    },
    {
        'name': 'state wrong number of arguments',
        'description': 'Tests if the state command with a wrong number of arguments is handled correctly.',
        'protocol': 'statewrongargsnum.txt'
    },
    {
        'name': 'state illegal arguments',
        'description': 'Tests if the state command with illegal arguments is handled correctly.',
        'protocol': 'stateillegalargs.txt'
    },
    {
        'name': 'print with arguments',
        'description': 'Tests if the print command with arguments is handled correctly.',
        'protocol': 'printargs.txt',
    },
    {
        'name': 'set-vc no arguments',
        'description': 'Tests if the set-vc command without arguments is handled correctly.',
        'protocol': 'setvcnoargs.txt'
    },
    {
        'name': 'set-vc wrong number of arguments',
        'description': 'Tests if the set-vc command with a wrong number of arguments is handled correctly.',
        'protocol': 'setvcwrongargsnum.txt'
    },
    {
        'name': 'set-vc illegal arguments',
        'description': 'Tests if the set-vc command with illegal arguments is handled correctly.',
        'protocol': 'setvcillegalargs.txt'
    },
    {
        'name': 'roll no arguments',
        'description': 'Tests if the roll command without arguments is handled correctly.',
        'protocol': 'rollnoargs.txt'
    },
    {
        'name': 'roll wrong number of arguments',
        'description': 'Tests if the set-vc command with a wrong number of arguments is handled correctly.',
        'protocol': 'rollwrongargsnum.txt'
    },
    {
        'name': 'roll illegal arguments',
        'description': 'Tests if the set-vc command with illegal arguments is handled correctly.',
        'protocol': 'rollillegalargs.txt'
    },
    {
        'name': 'place no arguments',
        'description': 'Tests if the place command without arguments is handled correctly.',
        'protocol': 'placenoargs.txt'
    },
    {
        'name': 'place wrong number of arguments',
        'description': 'Tests if the place command with a wrong number of arguments is handled correctly.',
        'protocol': 'placewrongargsnum.txt'
    },
    {
        'name': 'place illegal arguments',
        'description': 'Tests if the place command with illegal arguments is handled correctly.',
        'protocol': 'placeillegalargs.txt'
    },
    {
        'name': 'move no arguments',
        'description': 'Tests if the move command without arguments is handled correctly.',
        'protocol': 'movenoargs.txt'
    },
    {
        'name': 'move wrong number of arguments',
        'description': 'Tests if the move command with a wrong number of arguments is handled correctly.',
        'protocol': 'movewrongargsnum.txt'
    },
    {
        'name': 'move illegal arguments',
        'description': 'Tests if the move command with illegal arguments is handled correctly.',
        'protocol': 'moveillegalargs.txt'
    },
    {
        'name': 'show-result with arguments',
        'description': 'Tests if the show-result command with arguments is handled correctly.',
        'protocol': 'showresultargs.txt',
    },
    {
        'name': 'reset with arguments',
        'description': 'Tests if the reset command with arguments is handled correctly.',
        'protocol': 'resetargs.txt',
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
