#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': '0 arguments in file',
        'description': 'Tests if the command line parsing works correctly and exits because of invalid file content.',
        'protocol': 'fileError.txt',
		'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/private/error/file0Arguments.txt', 'r').read()},
    },
    {
        'name': '2 arguments in file',
        'description': 'Tests if the command line parsing works correctly and exits because of invalid file content.',
        'protocol': 'fileError.txt',
		'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/private/error/file2Arguments.txt', 'r').read()},
    },
    {
        'name': '4 arguments in file',
        'description': 'Tests if the command line parsing works correctly and exits because of invalid file content.',
        'protocol': 'fileError.txt',
		'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/private/error/file4Arguments.txt', 'r').read()},
    },
    {
        'name': '4 arguments in file, three of them valid',
        'description': 'Tests if the command line parsing works correctly and exits because of invalid file content.',
        'protocol': 'fileError.txt',
		'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/private/error/file4Arguments3Valid.txt', 'r').read()},
    },
    {
        'name': '3 arguments in file but 2 cars',
        'description': 'Tests if the command line parsing works correctly and exits because of invalid file content.',
        'protocol': 'fileError.txt',
		'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/private/error/file2Cars.txt', 'r').read()},
    },
    {
        'name': 'Invalid door numbers',
        'description': 'Tests if the command line parsing works correctly and wrong door numbers are handled correctly.',
        'protocol': 'wrongDoorNumber.txt',
		'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/private/error/standard3Rounds.txt', 'r').read()},
    },
    {
        'name': 'Invalid door change commands',
        'description': 'Tests if the command line parsing works correctly and wrong answers for the door change are handled correctly.',
        'protocol': 'yesNoInvalid.txt',
		'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/private/error/standard3Rounds.txt', 'r').read()},
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/error_semantic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="Tests if semantical errors are correctly handled.")
sys.exit(0 if success else 1)
