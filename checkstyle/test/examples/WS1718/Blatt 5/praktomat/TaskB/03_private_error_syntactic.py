#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'add-team no arguments',
        'description': 'Tests if the add-team command is handled correctly if arguments are missing.',
        'protocol': 'addteamnoargs.txt',
    },
    {
        'name': 'add-team wrong number of arguments',
        'description': 'Tests if the add-team command is handled correctly if the number of arguments is wrong.',
        'protocol': 'addteamwrongargsnum.txt',
    },
    {
        'name': 'add-team illegal arguments',
        'description': 'Tests if the add-team command is handled correctly if arguments are illegal.',
        'protocol': 'addteamillegalargs.txt',
    },
    {
        'name': 'list-team with arguments',
        'description': 'Tests if the list-team command with arguments is handled correctly.',
        'protocol': 'listteamargs.txt',
    },
    {
        'name': 'addmatch no arguments',
        'description': 'Tests if the addmatch command is handled correctly if arguments are missing.',
        'protocol': 'addmatchnoargs.txt',
    },
    {
        'name': 'addmatch wrong number of arguments',
        'description': 'Tests if the addmatch command is handled correctly if the number of arguments is wrong.',
        'protocol': 'addmatchwrongargsnum.txt',
    },
    {
        'name': 'addmatch illegal arguments',
        'description': 'Tests if the addmatch command is handled correctly if arguments are illegal.',
        'protocol': 'addmatchillegalargs.txt',
    },
    {
        'name': 'standings with arguments',
        'description': 'Tests if the standings command with arguments is handled correctly.',
        'protocol': 'standingsargs.txt',
    },
    {
        'name': 'quit with arguments',
        'description': 'Tests if the quit command with arguments is handled correctly.',
        'protocol': 'quitargs.txt',
    }
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/errsyntactic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="Tests if syntactic errors are correctly handled.")
sys.exit(0 if success else 1)
