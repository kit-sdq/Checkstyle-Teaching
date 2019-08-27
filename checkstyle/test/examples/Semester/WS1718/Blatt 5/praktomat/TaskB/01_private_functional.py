#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'list-team',
        'description': 'Tests if the list-team command works correctly.',
        'protocol': 'listteam.txt',
    },
    {
        'name': 'add-team space',
        'description': 'Tests if the add-team command works correctly if the name contains spaces.',
        'protocol': 'addteamspace.txt',
    },
    {
        'name': 'standings not all played',
        'description': 'Tests if the standings work correctly if some teams have no games.',
        'protocol': 'standingsincomplete.txt',
    },
    {
        'name': 'add team after games are played',
        'description': 'Tests if the standings work correctly when a team gets added later.',
        'protocol': 'standingsaddlater.txt',
    },
    {
        'name': 'addmatch 120 minutes',
        'description': 'Tests if the addmatch command works correctly a match with maximum match time is added.',
        'protocol': 'addmatchmax.txt',
    },
    {
        'name': 'standings same points',
        'description': 'Tests if the standings work correctly when two teams have the same points.',
        'protocol': 'standingssamepoints.txt',
    },
    {
        'name': 'standings same points and difference',
        'description': 'Tests if the standings work correctly when two teams have the same points and goal difference.',
        'protocol': 'standingssameboth.txt',
    },
    {
        'name': 'standings multi way tie',
        'description': 'Tests if the standings work correctly when multiple teams collide in points and/or goal difference.',
        'protocol': 'standingsmultitie.txt',
    },
    {
        'name': 'interactive',
        'description': 'Tests if long interactive usage works correctly and without commands influencing each other.',
        'protocol': 'interactive.txt',
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/functional/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="Tests if the functions of the program work correctly with correct input.")
sys.exit(0 if success else 1)
