#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'Invalid arguments in file - missing letters',
        'description': 'Tests if the command line parsing works correctly and exits because of invalid file content.',
        'protocol': 'fileError.txt',
		'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/private/error/fileInvalidNames.txt', 'r').read()},
    },
    {
        'name': 'Invalid arguments in file - integers',
        'description': 'Tests if the command line parsing works correctly and exits because of invalid file content.',
        'protocol': 'fileError.txt',
		'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/private/error/fileInvalidNamesNumbers.txt', 'r').read()},
    },
    {
        'name': 'Invalid arguments in file - special characters',
        'description': 'Tests if the command line parsing works correctly and exits because of invalid file content.',
        'protocol': 'fileError.txt',
		'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/private/error/fileInvalidNamesSpecialCharacter.txt', 'r').read()},
    },
    {
        'name': 'Invalid arguments in file - whitespaces',
        'description': 'Tests if the command line parsing works correctly and exits because of invalid file content.',
        'protocol': 'fileError.txt',
		'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/private/error/fileInvalidNamesWhitespace.txt', 'r').read()},
    },
    {
        'name': 'Quit with parameters',
        'description': 'Tests if the command line parsing works correctly and quit works as expected.',
        'protocol': 'quitWithParams.txt',
		'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/private/error/standard3Rounds.txt', 'r').read()},
    },
    {
        'name': 'Wrong door numbers - double',
        'description': 'Tests if the command line parsing works correctly and wrong door number are treated as expected.',
        'protocol': 'wrongDoorNumberDoubleComma.txt',
		'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/private/error/standard3Rounds.txt', 'r').read()},
    },
    {
        'name': 'Wrong door numbers - double 2',
        'description': 'Tests if the command line parsing works correctly and wrong door number are treated as expected.',
        'protocol': 'wrongDoorNumberDoubleDot.txt',
		'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/private/error/standard3Rounds.txt', 'r').read()},
    },
    {
        'name': 'Wrong door numbers - in brackets',
        'description': 'Tests if the command line parsing works correctly and wrong door number are treated as expected.',
        'protocol': 'wrongDoorNumberInBrackets.txt',
		'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/private/error/standard3Rounds.txt', 'r').read()},
    },
    {
        'name': 'Wrong door numbers - letter',
        'description': 'Tests if the command line parsing works correctly and wrong door number are treated as expected.',
        'protocol': 'wrongDoorNumberLetter.txt',
		'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/private/error/standard3Rounds.txt', 'r').read()},
    },
    {
        'name': 'Wrong door numbers - special characters',
        'description': 'Tests if the command line parsing works correctly and wrong door number are treated as expected.',
        'protocol': 'wrongDoorNumberSpecialCharacter.txt',
		'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/private/error/standard3Rounds.txt', 'r').read()},
    },
    {
        'name': 'Wrong door numbers - empty input',
        'description': 'Tests if the command line parsing works correctly and wrong door number are treated as expected.',
        'protocol': 'wrongDoorNumberMissing.txt',
		'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/private/error/standard3Rounds.txt', 'r').read()},
    },
    {
        'name': 'Invalid door change command arguments',
        'description': 'Tests if the command line parsing works correctly and yes or no are expected.',
        'protocol': 'yesNoInvalid.txt',
		'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/private/error/standard3Rounds.txt', 'r').read()},
    },

]

for test in tests:
    test['protocol'] = 'copy/protocols/private/error_syntactic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="Tests if syntactic errors are correctly handled.")
sys.exit(0 if success else 1)
