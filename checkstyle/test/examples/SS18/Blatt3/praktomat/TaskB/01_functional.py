#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'higher k',
        'description': 'Tests if the approximation works correctly with a higher k.',
        'protocol': 'highk.txt',
    },
    {
        'name': 'k = 1',
        'description': 'Tests if the approximation works correctly with a k equal to 1.',
        'protocol': 'k1.txt',
    },
    {
        'name': 'k = 0',
        'description': 'Tests if the approximation works correctly with a k equal to 0.',
        'protocol': 'k0.txt',
    }
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/functional/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if the program works correctly with correct input.")
sys.exit(0 if success else 1)
