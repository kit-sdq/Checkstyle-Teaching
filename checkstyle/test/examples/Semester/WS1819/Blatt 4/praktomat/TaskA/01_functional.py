#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'add test',
        'description': 'Tests if adding books and contacts succeed.',
        'protocol': 'add.txt',
    },
    {
        'name': 'remove test',
        'description': 'Tests if removing books and contacts succeed.',
        'protocol': 'remove.txt',
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/functional/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if the program works correctly with correct input.")
sys.exit(0 if success else 1)
