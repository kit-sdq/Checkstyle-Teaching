#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'rowprint',
        'description': 'Tests if the rowprint command works correctly.',
        'protocol': 'rowprint.txt',
        'arguments': 'standard 18 4'
    },
    {
        'name': 'rowprint after win',
        'description': 'Tests if the rowprint command works correctly after someone won.',
        'protocol': 'rowprintwin.txt',
        'arguments': 'standard 18 4'
    },
    {
        'name': 'rowprint bigger board',
        'description': 'Tests if the rowprint command works correctly on a 20x20 board.',
        'protocol': 'rowprintbig.txt',
        'arguments': 'standard 20 4'
    },
    {
        'name': 'colprint',
        'description': 'Tests if the colprint command works correctly.',
        'protocol': 'colprint.txt',
        'arguments': 'standard 18 4'
    },
    {
        'name': 'colprint win',
        'description': 'Tests if the colprint command works correctly after someone won.',
        'protocol': 'colprintwin.txt',
        'arguments': 'standard 18 4'
    },
    {
        'name': 'colprint bigger board',
        'description': 'Tests if the colprint command works correctly on a 20x20 board.',
        'protocol': 'colprintbig.txt',
        'arguments': 'standard 20 4'
    },
    {
        'name': 'print',
        'description': 'Tests if the print command works correctly.',
        'protocol': 'print.txt',
        'arguments': 'standard 18 4'
    },
    {
        'name': 'print win',
        'description': 'Tests if the print command works correctly after someone won.',
        'protocol': 'printwin.txt',
        'arguments': 'standard 18 4'
    },
    {
        'name': 'print bigger board',
        'description': 'Tests if the print command works correctly on a 20x20 board.',
        'protocol': 'printbig.txt',
        'arguments': 'standard 20 4'
    },
    {
        'name': 'print after draw',
        'description': 'Tests if printing a full board works correctly.',
        'protocol': 'printdraw.txt',
        'arguments': 'standard 18 4'
    },
    {
        'name': 'print after draw bigger board',
        'description': 'Tests if printing a full board works correctly on a 20x20 board.',
        'protocol': 'printdrawbig.txt',
        'arguments': 'standard 20 4'
    },
    {
        'name': 'state',
        'description': 'Tests if the state command works correctly.',
        'protocol': 'state.txt',
        'arguments': 'standard 18 4'
    },
    {
        'name': 'state win',
        'description': 'Tests if the state command works correctly after someone won.',
        'protocol': 'statewin.txt',
        'arguments': 'standard 18 4'
    },
    {
        'name': 'state torus',
        'description': 'Tests if the state command works correctly in torus mode.',
        'protocol': 'statetorus.txt',
        'arguments': 'torus 18 4'
    },
    {
        'name': 'state bigger board',
        'description': 'Tests if the state command works correctly on a bigger board.',
        'protocol': 'statebig.txt',
        'arguments': 'standard 20 4'
    },
    {
        'name': 'reset',
        'description': 'Tests if the reset command works correctly.',
        'protocol': 'reset.txt',
        'arguments': 'standard 18 4'
    },
    {
        'name': 'reset after win',
        'description': 'Tests if the reset command works correctly after someone won.',
        'protocol': 'resetwin.txt',
        'arguments': 'standard 18 4'
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/interaction/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if all other commands and interactions work correctly.")
sys.exit(0 if success else 1)
