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
        'name': 'start no argument',
        'description': 'Tests if the start command handles no provided arguments correctly.',
        'protocol': 'startnoargs.txt',
    },
    {
        'name': 'start illegal arguments',
        'description': 'Tests if the start command handles illegal arguments correctly.',
        'protocol': 'startillegalargs.txt',
    },
    {
        'name': 'start illegal number of arguments',
        'description': 'Tests if the start command handles too many arguments correctly.',
        'protocol': 'startwrongargsnum.txt',
    },
    {
        'name': 'place no argument',
        'description': 'Tests if the place command handles no provided arguments correctly.',
        'protocol': 'placenoargs.txt',
    },
    {
        'name': 'place illegal arguments',
        'description': 'Tests if the place command handles illegal arguments correctly.',
        'protocol': 'placeillegalargs.txt',
    },
    {
        'name': 'place illegal number of arguments',
        'description': 'Tests if the place command handles too many or too less arguments correctly.',
        'protocol': 'placewrongargsnum.txt',
    },
    {
        'name': 'move no argument',
        'description': 'Tests if the move command handles no provided arguments correctly.',
        'protocol': 'movenoargs.txt',
    },
    {
        'name': 'pass with arguments',
        'description': 'Tests if the pass command handles provided arguments correctly.',
        'protocol': 'passargs.txt',
    },
    {
        'name': 'print with arguments',
        'description': 'Tests if the print command handles provided arguments correctly.',
        'protocol': 'printargs.txt',
    },
    {
        'name': 'quit with arguments',
        'description': 'Tests if the quit command handles provided arguments correctly.',
        'protocol': 'quitargs.txt',
    }
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/error_syntactic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if wrong input is handled correctly.")
sys.exit(0 if success else 1)
