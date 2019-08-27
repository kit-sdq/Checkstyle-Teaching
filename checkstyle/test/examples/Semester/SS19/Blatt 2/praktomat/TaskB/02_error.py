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
        'description': 'Tests if the digitsum command works correctly with invalid input.',
        'protocol': 'err_digitsum.txt',
    },
	{
        'name': 'checksum examples',
        'description': 'Tests if the checksum command works correctly with invalid input.',
        'protocol': 'err_checksum.txt',
    },
	{
        'name': 'quit examples',
        'description': 'Tests if the quit command works correctly with invalid input.',
        'protocol': 'err_quit.txt',
    },
    {
        'name': 'isValid examples',
        'description': 'Tests if the isValid command works correctly with invalid input.',
        'protocol': 'err_isValid.txt',
    }
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/err/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="Tests if semantical errors are correctly handled.")
sys.exit(0 if success else 1)
