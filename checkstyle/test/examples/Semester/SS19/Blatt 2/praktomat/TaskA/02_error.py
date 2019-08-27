#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import interactive

tests = [
    {
        'name': "Different invalid input values",
        'description': "Checks if some examples work correctly.",
        'protocol': 'error.txt',
    }
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/err/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                  description="These are tests to check the functionality of the program with correct input.")
sys.exit(0 if success else 1)
