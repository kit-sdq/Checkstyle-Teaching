#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'enter start after winning',
        'description': 'Tests if entering start after a finished game is handled correctly.',
        'protocol': 'startAfter.txt',
		'arguments': 'standard 5',
    },
	{
        'name': 'enter token after winning',
        'description': 'Tests if entering token after a finished game is handled correctly.',
        'protocol': 'tokenAfter.txt',
		'arguments': 'standard 5',
    },
	{
        'name': 'moving outside vertical in standard mode',
        'description': 'Tests if moving around the board does not work in standard.',
        'protocol': 'outsideVertical.txt',
		'arguments': 'standard 5',
    },
	{
        'name': 'placing outside in standard mode',
        'description': 'Tests if placing outside the board does not work in standard.',
        'protocol': 'torusOutside.txt',
		'arguments': 'standard 5',
    },
	{
        'name': 'commands without start',
        'description': 'Tests if commands work correctly without previous start command.',
        'protocol': 'withoutStart.txt',
		'arguments': 'standard 5',
    },
	{
        'name': 'move without roll',
        'description': 'Tests if move without previous roll does not work.',
        'protocol': 'moveWithoutRoll.txt',
		'arguments': 'standard 5',
    },
	{
        'name': 'move with wrong token',
        'description': 'Tests if moving the wrong token does not work.',
        'protocol': 'moveWrongToken.txt',
		'arguments': 'standard 5',
    },
	{
        'name': 'move in an invalid direction',
        'description': 'Tests if moving in invalid directions does not work.',
        'protocol': 'moveWrongDirection.txt',
		'arguments': 'standard 5',
    },
	{
        'name': 'roll after a failed move',
        'description': 'Tests if roll can be called again after a failed move.',
        'protocol': 'rollAfterFailedMove.txt',
		'arguments': 'standard 5',
    },
	{
        'name': 'the token cannot stay on the same tile',
        'description': 'Tests if not moving a token produces an error.',
        'protocol': 'dontMove.txt',
		'arguments': 'standard 5',
    },
	{
        'name': 'roll again',
        'description': 'Tests if rolling again without moving is not possible.',
        'protocol': 'rollAgain.txt',
		'arguments': 'standard 5',
    },
	{
        'name': 'move again',
        'description': 'Tests if moving again is not possible.',
        'protocol': 'moveAgain.txt',
		'arguments': 'standard 5',
    },
	
	
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/error_semantic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="Tests if semantical errors are correctly handled.")
sys.exit(0 if success else 1)
