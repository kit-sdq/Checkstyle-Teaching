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
        'name': 'create no argument',
        'description': 'Tests if the create command handles no provided arguments correctly.',
        'protocol': 'createnoargs.txt',
    },
    {
        'name': 'create illegal argument',
        'description': 'Tests if the create command handles illegal arguments correctly.',
        'protocol': 'createillegalargs.txt',
    },
    {
        'name': 'create too many arguments',
        'description': 'Tests if the create command handles too many arguments correctly.',
        'protocol': 'createtoomanyargs.txt',
    },
    {
        'name': 'reset no argument',
        'description': 'Tests if the reset command handles no provided arguments correctly.',
        'protocol': 'resetnoargs.txt',
    },
    {
        'name': 'reset illegal argument',
        'description': 'Tests if the reset command handles illegal arguments correctly.',
        'protocol': 'resetillegalargs.txt',
    },
    {
        'name': 'reset too many arguments',
        'description': 'Tests if the reset command handles too many arguments correctly.',
        'protocol': 'resettoomanyargs.txt',
    },
    {
        'name': 'add no argument',
        'description': 'Tests if the add command handles no provided arguments correctly.',
        'protocol': 'addnoargs.txt',
    },
    {
        'name': 'add illegal argument',
        'description': 'Tests if the add command handles illegal arguments correctly.',
        'protocol': 'addillegalargs.txt',
    },
    {
        'name': 'add wrong number of arguments',
        'description': 'Tests if the add command handles a wrong number of arguments correctly.',
        'protocol': 'addwrongnumargs.txt',
    },
    {
        'name': 'modify no argument',
        'description': 'Tests if the modify command handles no provided arguments correctly.',
        'protocol': 'modifynoargs.txt',
    },
    {
        'name': 'modify illegal argument',
        'description': 'Tests if the modify command handles illegal arguments correctly.',
        'protocol': 'modifyillegalargs.txt',
    },
    {
        'name': 'modify wrong number of arguments',
        'description': 'Tests if the modify command handles a wrong number of arguments correctly.',
        'protocol': 'modifywrongnumargs.txt',
    },
    {
        'name': 'delete no argument',
        'description': 'Tests if the delete command handles no provided arguments correctly.',
        'protocol': 'deletenoargs.txt',
    },
    {
        'name': 'delete illegal argument',
        'description': 'Tests if the delete command handles illegal arguments correctly.',
        'protocol': 'deleteillegalargs.txt',
    },
    {
        'name': 'delete wrong number of arguments',
        'description': 'Tests if the delete command handles a wrong number of arguments correctly.',
        'protocol': 'deletewrongnumargs.txt',
    },
    {
        'name': 'credits no argument',
        'description': 'Tests if the credits command handles no provided arguments correctly.',
        'protocol': 'creditsnoargs.txt',
    },
    {
        'name': 'credits illegal argument',
        'description': 'Tests if the credits command handles illegal arguments correctly.',
        'protocol': 'creditsillegalargs.txt',
    },
    {
        'name': 'credits wrong number of arguments',
        'description': 'Tests if the credits command handles a wrong number of arguments correctly.',
        'protocol': 'creditswrongnumargs.txt',
    },
    {
        'name': 'print no argument',
        'description': 'Tests if the print command handles no provided arguments correctly.',
        'protocol': 'printnoargs.txt',
    },
    {
        'name': 'print illegal argument',
        'description': 'Tests if the print command handles illegal arguments correctly.',
        'protocol': 'printillegalargs.txt',
    },
    {
        'name': 'print too many arguments',
        'description': 'Tests if the print command handles too many arguments correctly.',
        'protocol': 'printtoomanyargs.txt',
    },
    {
        'name': 'average no argument',
        'description': 'Tests if the average command handles no provided arguments correctly.',
        'protocol': 'averagenoargs.txt',
    },
    {
        'name': 'average illegal argument',
        'description': 'Tests if the average command handles illegal arguments correctly.',
        'protocol': 'averageillegalargs.txt',
    },
    {
        'name': 'average too many arguments',
        'description': 'Tests if the average command handles too many arguments correctly.',
        'protocol': 'averagetoomanyargs.txt',
    },
    {
        'name': 'median no argument',
        'description': 'Tests if the median command handles no provided arguments correctly.',
        'protocol': 'mediannoargs.txt',
    },
    {
        'name': 'median illegal argument',
        'description': 'Tests if the median command handles illegal arguments correctly.',
        'protocol': 'medianillegalargs.txt',
    },
    {
        'name': 'median too many arguments',
        'description': 'Tests if the median command handles too many arguments correctly.',
        'protocol': 'mediantoomanyargs.txt',
    },
    {
        'name': 'strings with spaces',
        'description': 'Tests if the string inputs handle spaces correctly.',
        'protocol': 'spaces.txt',
    },
    {
        'name': 'quit with arguments',
        'description': 'Tests if the quit command handles provided arguments correctly.',
        'protocol': 'quitwithargs.txt',
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/error_syntactic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if wrong input is handled correctly.")
sys.exit(0 if success else 1)
