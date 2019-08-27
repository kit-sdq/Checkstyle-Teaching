#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import interactive

tests = [
	{
        'name': 'Public with torus and prints',
        'description': 'Tests if basic interaction prints work correctly.',
        'protocol': 'public.txt',
		'arguments': 'torus 5',
    },
    {
        'name': 'Correct parameters',
        'description': 'Tests if parameter handling works correctly.',
        'protocol': 'quit.txt',
		'arguments': 'torus 5',
    },
	{
        'name': 'Correct parameters',
        'description': 'Tests if parameter handling works correctly.',
        'protocol': 'quit.txt',
		'arguments': 'torus 7',
    },
	{
        'name': 'Correct parameters',
        'description': 'Tests if parameter handling works correctly.',
        'protocol': 'quit.txt',
		'arguments': 'torus 5',
    },
	{
        'name': 'Correct parameters',
        'description': 'Tests if parameter handling works correctly.',
        'protocol': 'quit.txt',
		'arguments': 'torus 7',
    },
	{
        'name': 'start',
        'description': 'Tests if start and print work correctly.',
        'protocol': 'start.txt',
		'arguments': 'torus 7',
    },
	{
        'name': 'start 2',
        'description': 'Tests if start and print work correctly.',
        'protocol': 'start2.txt',
		'arguments': 'torus 7',
    },
	{
        'name': 'capture',
        'description': 'Tests if capturing works correctly.',
        'protocol': 'capture.txt',
		'arguments': 'torus 5',
    },
	{
        'name': 'empty upper part',
        'description': 'Tests if choosing the correct token works with an empty upper half.',
        'protocol': 'emptySet.txt',
		'arguments': 'torus 5',
    },
	{
        'name': 'choose token',
        'description': 'Tests if choosing the correct token works with both sides.',
        'protocol': 'choose.txt',
		'arguments': 'torus 5',
    },
	{
        'name': 'all tokens captured',
        'description': 'Tests if winning with all tokens captured works.',
        'protocol': 'allTokens.txt',
		'arguments': 'torus 5',
    },
	{
        'name': 'print-cell',
        'description': 'Tests if print-cell works.',
        'protocol': 'printcell.txt',
		'arguments': 'torus 7',
    },
	{
        'name': 'token',
        'description': 'Tests if token works.',
        'protocol': 'token.txt',
		'arguments': 'torus 5',
    },
	{
        'name': 'token 2',
        'description': 'Tests if token works.',
        'protocol': 'allTokenstoken.txt',
		'arguments': 'torus 5',
    },
	{
        'name': 'Game with size 7',
        'description': 'Tests if abasic game with bigger board works correctly.',
        'protocol': 'basic7.txt',
		'arguments': 'torus 7',
    },
	{
        'name': 'placing outside',
        'description': 'Tests if placing outside of the board works correctly.',
        'protocol': 'torusOutside.txt',
		'arguments': 'torus 5',
    },
	{
        'name': 'moving outside horizontal',
        'description': 'Tests if moving around the board works correctly.',
        'protocol': 'outsideHorizontal.txt',
		'arguments': 'torus 5',
    },
	{
        'name': 'moving outside vertical',
        'description': 'Tests if moving around the board works correctly.',
        'protocol': 'outsideVertical.txt',
		'arguments': 'torus 5',
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/functional/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                  description="These are tests to check the functionality of the program with correct input.")
sys.exit(0 if success else 1)
