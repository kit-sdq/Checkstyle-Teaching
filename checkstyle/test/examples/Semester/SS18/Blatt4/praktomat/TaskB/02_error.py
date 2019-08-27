#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'File nonexisting shape',
        'description': 'Tests a nonexisting shape in the file is handled correctly.',
        'protocol': 'fileerror.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/private/error/nonexistingshape.txt', 'r').read()},
    },
    {
        'name': 'File digits as shape',
        'description': 'Tests a digit instead of a shape in the file is handled correctly.',
        'protocol': 'fileerror.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/private/error/digitasshape.txt', 'r').read()},
    },
    {
        'name': 'File empty',
        'description': 'Tests an empty file is handled correctly.',
        'protocol': 'fileerror.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/private/error/empty.txt', 'r').read()},
    },
    {
        'name': 'File empty line',
        'description': 'Tests an empty line in the file is handled correctly.',
        'protocol': 'fileerror.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/private/error/emptyline.txt', 'r').read()},
    },
    {
        'name': 'File wrong line',
        'description': 'Tests if a garbage line in the file is handled correctly.',
        'protocol': 'fileerror.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/private/error/garbage.txt', 'r').read()},
    },
    {
        'name': 'File too many shapes',
        'description': 'Tests if too many shapes in the file are handled correctly.',
        'protocol': 'fileerror.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/private/error/toomanyshapes.txt', 'r').read()},
    },
    {
        'name': 'File too few shapes',
        'description': 'Tests too few shapes in the file are handled correctly.',
        'protocol': 'fileerror.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/private/error/toofewshapes.txt', 'r').read()},
    },
    {
        'name': 'Input wrong shape count',
        'description': 'Tests if a wrong amount of shapes is handled correctly.',
        'protocol': 'wrongshapecount.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/public/public.txt', 'r').read()},
    },
    {
        'name': 'Input nonexistant shape',
        'description': 'Tests if a nonexistant shape is handled correctly.',
        'protocol': 'nonexistantshape.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/public/public.txt', 'r').read()},
    },
    {
        'name': 'Input invalid',
        'description': 'Tests if invalid input is handled correctly.',
        'protocol': 'invalidinput.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/public/public.txt', 'r').read()},
    },
    {
        'name': 'Input after game is over',
        'description': 'Tests if input after the game is over is handled correctly.',
        'protocol': 'inputaftergameover.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/public/public.txt', 'r').read()},
    },
    {
        'name': 'quit with params',
        'description': 'Tests the quit command with parameters provided is handled correctly.',
        'protocol': 'quitparams.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/public/public.txt', 'r').read()},
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/error/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="Tests if semantical or syntactical errors are correctly handled.")
sys.exit(0 if success else 1)
