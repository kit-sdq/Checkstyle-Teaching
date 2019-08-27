#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'approximation negative k',
        'description': 'Tests if a negative k is handled correctly.',
        'protocol': 'negativek.txt',
    },
    {
        'name': 'approximation invalid k',
        'description': 'Tests if an invalid k is handled correctly.',
        'protocol': 'invalidk.txt',
    },
    {
        'name': 'approximation too many parameters',
        'description': 'Tests if too many parameters are handled correctly.',
        'protocol': 'toomanyparams.txt',
    },
    {
        'name': 'approximation no params',
        'description': 'Tests if no parameters given is handled correctly.',
        'protocol': 'noparams.txt',
    },
    {
        'name': 'quit parameters',
        'description': 'Tests if quit with parameters is handled correctly.',
        'protocol': 'quitparams.txt',
    }
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/err/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="Tests if semantical errors are correctly handled.")
sys.exit(0 if success else 1)
