#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'add existing flight number',
        'description': 'Tests if adding a flight with an existing number is handled correctly.',
        'protocol': 'addexistingid.txt',
    },
    {
        'name': 'add nonexisting currency',
        'description': 'Tests if adding a flight with a nonexisting currency is handled correctly.',
        'protocol': 'addnonexistingcurrency.txt',
    },
    {
        'name': 'remove unknown flight number',
        'description': 'Tests if removing a flight with an unknown number is handled correctly.',
        'protocol': 'removeunknownid.txt',
    },
    {
        'name': 'book unknown flight number',
        'description': 'Tests if booking a flight with an unknown number is handled correctly.',
        'protocol': 'bookunknownid.txt',
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/error_semantic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="Tests if semantical errors are correctly handled.")
sys.exit(0 if success else 1)
