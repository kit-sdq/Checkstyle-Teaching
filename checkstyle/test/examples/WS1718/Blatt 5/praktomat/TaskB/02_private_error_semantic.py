#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'add-team invalid id',
        'description': 'Tests if the add-team command is handled correctly if the id is invalid.',
        'protocol': 'addteaminvalidid.txt',
    },
    {
        'name': 'add-team id exists',
        'description': 'Tests if the add-team command is handled correctly if a team with the id already exists.',
        'protocol': 'addteamidexists.txt',
    },
    {
        'name': 'add-team name exists',
        'description': 'Tests if the add-team command is handled correctly if a team with the name already exists.',
        'protocol': 'addteamnameexists.txt',
    },
    {
        'name': 'addmatch invalid id',
        'description': 'Tests if the addmatch command is handled correctly if the id of a team is invalid.',
        'protocol': 'addmatchinvalidid.txt',
    },
    {
        'name': 'addmatch id team doesn\'t exist',
        'description': 'Tests if the addmatch command is handled correctly if the id of a team doesn\'t exist.',
        'protocol': 'addmatchnonexistingid.txt',
    },
    {
        'name': 'addmatch illegal gametime',
        'description': 'Tests if the addmatch command is handled correctly if the gametime is not allowed.',
        'protocol': 'addmatchillegalgametime.txt',
    },
    {
        'name': 'addmatch draw',
        'description': 'Tests if the addmatch command is handled correctly if the game is a draw.',
        'protocol': 'addmatchillegalgametime.txt',
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/errsemantic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="Tests if semantic errors are correctly handled.")
sys.exit(0 if success else 1)
