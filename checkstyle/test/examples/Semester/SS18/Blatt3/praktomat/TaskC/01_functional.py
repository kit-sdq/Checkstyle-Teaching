#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'Winning directly',
        'description': 'Tests if winning directly works correctly.',
        'protocol': 'directwin1.txt',
        'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/directwin1.txt', 'r').read()},
    },
    {
        'name': 'Winning directly different colors',
        'description': 'Tests if winning directly works correctly with four different colors.',
        'protocol': 'directwin2.txt',
        'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/directwin2.txt', 'r').read()},
    },
    {
        'name': 'Winning directly last two colors',
        'description': 'Tests if winning directly works correctly with the last two colors.',
        'protocol': 'directwin3.txt',
        'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/directwin3.txt', 'r').read()},
    },
    {
        'name': 'Completely wrong guess',
        'description': 'Tests if a completely wrong guess is evaluated correctly.',
        'protocol': 'allwrong.txt',
        'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/directwin1.txt', 'r').read()},
    },
    {
        'name': 'All colors correct, but wrong places',
        'description': 'Tests if all colors correct but all at wrong places is evaluated correctly.',
        'protocol': 'correctbutwrongplace.txt',
        'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/directwin1.txt', 'r').read()},
    },
    {
        'name': 'All correct but one',
        'description': 'Tests if an all correct but one guess is evaluated correctly.',
        'protocol': 'allbutone.txt',
        'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/directwin1.txt', 'r').read()},
    },
    {
        'name': 'Winning after some tries',
        'description': 'Tests if winning after some tries works correctly.',
        'protocol': 'sometries.txt',
        'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/directwin1.txt', 'r').read()},
    },
    {
        'name': 'Double color',
        'description': 'Tests if evaluation and winning with double colors works correctly.',
        'protocol': 'doublecolors.txt',
        'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/doublecolors.txt', 'r').read()},
    },
    {
        'name': 'All colors the same',
        'description': 'Tests if evaluation and winning with all colors the same works correctly.',
        'protocol': 'allsame.txt',
        'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/allsame.txt', 'r').read()},
    },
    {
        'name': 'Winning last try',
        'description': 'Tests if winning with the last try works correctly.',
        'protocol': 'lasttry.txt',
        'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/directwin1.txt', 'r').read()},
    },
    {
        'name': 'Lose',
        'description': 'Tests if losing works correctly.',
        'protocol': 'lose.txt',
        'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/directwin1.txt', 'r').read()},
    }
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/functional/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if the program works correctly with correct input.")
sys.exit(0 if success else 1)
