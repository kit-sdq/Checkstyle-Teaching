#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'Public Test',
        'description': 'Tests if placing stones, winning the game and printing the board works correctly for a specific case.',
        'protocol': 'public.txt',
        'arguments': 'standard 18 2'
    }
]

for test in tests:
    test['protocol'] = 'copy/protocols/public/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These are public tests. Remember, we have got a lot more than just these.")
sys.exit(0 if success else 1)
