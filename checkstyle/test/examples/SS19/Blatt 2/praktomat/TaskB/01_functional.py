#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'digitsum examples',
        'description': 'Tests if the digitsum command works correctly.',
        'protocol': 'functional_digitsum.txt',
    },
	{
        'name': 'checksum examples',
        'description': 'Tests if the checksum command works correctly.',
        'protocol': 'functional_checksum.txt',
    },
    {
        'name': 'isValid examples',
        'description': 'Tests if the isValid command works correctly.',
        'protocol': 'functional_isValid.txt',
    }
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/functional/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if the program works correctly with correct input.")
sys.exit(0 if success else 1)
