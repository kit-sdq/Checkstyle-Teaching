#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'Narcisstic number with base 10',
        'description': 'Tests if the algorithm works correctly with base 10.',
        'protocol': 'functional_10.txt',
    },
    {
        'name': 'Narcisstic number with base other than 10',
        'description': 'Tests if the algorithm works correctly with base other than 10.',
        'protocol': 'functional_other.txt',
    }
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/functional/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if the program works correctly with correct input.")
sys.exit(0 if success else 1)
