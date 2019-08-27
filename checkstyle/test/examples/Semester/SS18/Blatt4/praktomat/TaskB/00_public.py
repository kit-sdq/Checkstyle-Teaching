#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'Simple RPS game',
        'description': 'Tests if winning an RPS game with one round works correctly.',
        'protocol': 'public.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/public/public.txt', 'r').read()},
    }
]

for test in tests:
    test['protocol'] = 'copy/protocols/public/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These are public tests. Remember, we have got a lot more than just these.")
sys.exit(0 if success else 1)
