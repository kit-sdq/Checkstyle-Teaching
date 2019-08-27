#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'No door change winning',
        'description': 'Tests if command line parsing and winning with no door change work.',
        'protocol': 'winningNotChanging.txt',
		'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/private/functional/winningNotChanging.txt', 'r').read()},
    },
    {
        'name': 'No door change losing',
        'description': 'Tests if the command line parsing and losing works correctly if you do not change the door.',
        'protocol': 'losingNotChanging.txt',
		'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/private/functional/losingNotChanging.txt', 'r').read()},
    },
    {
        'name': 'Door change winning',
        'description': 'Tests if the command line parsing and winning works correctly if you change the door.',
        'protocol': 'winningChanging.txt',
		'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/private/functional/winningChanging.txt', 'r').read()},
    },
    {
        'name': 'Door change losing',
        'description': 'Tests if the command line parsing and losing works correctly if you change the door.',
        'protocol': 'losingChanging.txt',
		'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/private/functional/losingChanging.txt', 'r').read()},
    },
    {
        'name': 'Door change winning and losing',
        'description': 'Tests if the command line parsing and winning and losing works correctly.',
        'protocol': 'winningAndLosing.txt',
		'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/private/functional/winningAndLosing.txt', 'r').read()},
    },
    {
        'name': 'No door change winning for 41 rounds',
        'description': 'Tests if the command line parsing and winning 41 rounds works correctly.',
        'protocol': '41RoundsWinning.txt',
		'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/private/functional/41RoundsWinning.txt', 'r').read()},
    },
    {
        'name': 'Quit within round',
        'description': 'Tests if the quit command is working as expected if used within a round.',
        'protocol': 'quitInRound.txt',
		'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/private/functional/quitInRound.txt', 'r').read()},
    },
    {
        'name': 'Quit between rounds',
        'description': 'Tests if the quit command is working as expected between two rounds.',
        'protocol': 'quitBetweenRounds.txt',
		'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/private/functional/quitBetweenRounds.txt', 'r').read()},
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/functional/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if the program works correctly with correct input.")
sys.exit(0 if success else 1)
