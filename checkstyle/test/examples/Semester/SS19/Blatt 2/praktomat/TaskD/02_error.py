#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'Wrong input format',
        'description': 'Tests if invalid input is handled correctly.',
        'protocol': 'err.txt',
    },
    {
        'name': 'Wrong input format for quit',
        'description': 'Tests if invalid input is handled correctly.',
        'protocol': 'err_quit.txt',
    }
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/err/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="Tests if semantical errors are correctly handled.")
sys.exit(0 if success else 1)
