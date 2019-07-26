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
        'name': 'no command line arguments',
        'description': 'Tests if no command line arguments at all are handled correctly.',
        'protocol': 'error.txt',
    },
    {
        'name': 'too many command line arguments',
        'description': 'Tests if too many command line arguments is handled correctly.',
        'protocol': 'error.txt',
        'arguments': 'standard 33 42',
    },
    {
        'name': 'missing game mode',
        'description': 'Tests if a missing game mode is handled correctly.',
        'protocol': 'error.txt',
        'arguments': '32',
    },
    {
        'name': 'missing token count',
        'description': 'Tests if a missing token count is handled correctly.',
        'protocol': 'error.txt',
        'arguments': 'standard',
    },
    {
        'name': 'throwin no argument',
        'description': 'Tests if the throwin command handles no provided arguments correctly.',
        'protocol': 'throwinnoargs.txt',
        'arguments': 'standard 32',
    },
    {
        'name': 'throwin illegal argument',
        'description': 'Tests if the throwin command handles illegal arguments correctly.',
        'protocol': 'throwinillegalargs.txt',
        'arguments': 'standard 32',
    },
    {
        'name': 'throwin too many arguments',
        'description': 'Tests if the throwin command handles too many arguments correctly.',
        'protocol': 'throwintoomanyargs.txt',
        'arguments': 'standard 32',
    },
    {
        'name': 'flip with arguments',
        'description': 'Tests if the flip command handles provided arguments correctly.',
        'protocol': 'flipwithargs.txt',
        'arguments': 'flip 32',
    },
    {
        'name': 'remove no argument',
        'description': 'Tests if the remove command handles no provided arguments correctly.',
        'protocol': 'removenoargs.txt',
        'arguments': 'remove 32',
    },
    {
        'name': 'remove illegal argument',
        'description': 'Tests if the remove command handles illegal arguments correctly.',
        'protocol': 'removeillegalargs.txt',
        'arguments': 'remove 32',
    },
    {
        'name': 'remove too many arguments',
        'description': 'Tests if the remove command handles too many arguments correctly.',
        'protocol': 'removetoomanyargs.txt',
        'arguments': 'remove 32',
    },
    {
        'name': 'token with arguments',
        'description': 'Tests if the token command handles provided arguments correctly.',
        'protocol': 'tokenwithargs.txt',
        'arguments': 'standard 32',
    },
    {
        'name': 'state no argument',
        'description': 'Tests if the state command handles no provided arguments correctly.',
        'protocol': 'statenoargs.txt',
        'arguments': 'standard 32',
    },
    {
        'name': 'state illegal argument',
        'description': 'Tests if the state command handles illegal arguments correctly.',
        'protocol': 'stateillegalargs.txt',
        'arguments': 'standard 32',
    },
    {
        'name': 'state too many arguments',
        'description': 'Tests if the state command handles too many arguments correctly.',
        'protocol': 'statetoomanyargs.txt',
        'arguments': 'standard 32',
    },
    {
        'name': 'print with arguments',
        'description': 'Tests if the print command handles provided arguments correctly.',
        'protocol': 'printwithargs.txt',
        'arguments': 'standard 32',
    },
    {
        'name': 'quit with arguments',
        'description': 'Tests if the quit command handles provided arguments correctly.',
        'protocol': 'quitwithargs.txt',
        'arguments': 'standard 32',
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/error_syntactic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if wrong input is handled correctly.")
sys.exit(0 if success else 1)
