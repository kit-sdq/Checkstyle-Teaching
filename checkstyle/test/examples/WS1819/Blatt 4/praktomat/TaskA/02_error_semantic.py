#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'add existing contact or book',
        'description': 'Tests if adding an already existing contact or book is handled correctly.',
        'protocol': 'addexisting.txt',
    },
    {
        'name': 'remove nonexisting contact or book',
        'description': 'Tests if removing a nonexisting contact or book is handled correctly.',
        'protocol': 'removenonexisting.txt',
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/error_semantic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="Tests if semantical errors are correctly handled.")
sys.exit(0 if success else 1)
