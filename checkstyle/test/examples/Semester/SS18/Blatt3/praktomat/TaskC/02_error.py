#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'File nonexisting color',
        'description': 'Tests a nonexisting color in the file is handled correctly.',
        'protocol': 'fileerror.txt',
        'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/error/nonexistingcolor.txt', 'r').read()},
    },
    {
        'name': 'File digits as color',
        'description': 'Tests a digit instead of a color in the file is handled correctly.',
        'protocol': 'fileerror.txt',
        'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/error/digitascolor.txt', 'r').read()},
    },
    {
        'name': 'File empty',
        'description': 'Tests an empty file is handled correctly.',
        'protocol': 'fileerror.txt',
        'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/error/empty.txt', 'r').read()},
    },
    {
        'name': 'File empty line',
        'description': 'Tests an empty line in the file is handled correctly.',
        'protocol': 'fileerror.txt',
        'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/error/emptyline.txt', 'r').read()},
    },
    {
        'name': 'File additional lines',
        'description': 'Tests a file with too many lines is handled correctly.',
        'protocol': 'fileerror.txt',
        'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/error/additionallines.txt', 'r').read()},
    },
    {
        'name': 'File additional empty line',
        'description': 'Tests an additional empty line at the end is handled correctly.',
        'protocol': 'fileerror.txt',
        'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/error/additionalemptyline.txt', 'r').read()},
    },
    {
        'name': 'File wrong line',
        'description': 'Tests if a garbage line in the file is handled correctly.',
        'protocol': 'fileerror.txt',
        'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/error/garbage.txt', 'r').read()},
    },
    {
        'name': 'File too many colors',
        'description': 'Tests if too many colors in the file are handled correctly.',
        'protocol': 'fileerror.txt',
        'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/error/toomanycolors.txt', 'r').read()},
    },
    {
        'name': 'File too few colors',
        'description': 'Tests too few colors in the file are handled correctly.',
        'protocol': 'fileerror.txt',
        'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/error/toofewcolors.txt', 'r').read()},
    },
    {
        'name': 'Guess wrong color count',
        'description': 'Tests if a wrong amount of colors is handled correctly.',
        'protocol': 'wrongcolorcount.txt',
        'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/directwin1.txt', 'r').read()},
    },
    {
        'name': 'Guess nonexistant color',
        'description': 'Tests if a nonexistant colors are handled correctly.',
        'protocol': 'nonexistantcolor.txt',
        'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/directwin1.txt', 'r').read()},
    },
    {
        'name': 'Guess invalid input',
        'description': 'Tests if invalid input is handled correctly.',
        'protocol': 'invalidinput.txt',
        'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/directwin1.txt', 'r').read()},
    },
    {
        'name': 'Guess after game is over',
        'description': 'Tests if a guess after the game is over is handled correctly.',
        'protocol': 'guessaftergameover.txt',
        'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/directwin1.txt', 'r').read()},
    },
    {
        'name': 'quit with params',
        'description': 'Tests the quit command with parameters provided is handled correctly.',
        'protocol': 'quitparams.txt',
        'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/directwin1.txt', 'r').read()},
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/error/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="Tests if semantical or syntactical errors are correctly handled.")
sys.exit(0 if success else 1)
