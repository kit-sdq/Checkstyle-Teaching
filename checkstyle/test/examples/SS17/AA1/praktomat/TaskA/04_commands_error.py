#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
		'name'        : 'move no step count',
		'description' : 'Tests if the move command prints an error if no step count is given.',
		'arguments'   : 'standard 4 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'move_no_stepcount.txt',
	},
	{
		'name'        : 'move illegal step count',
		'description' : 'Tests if the move command prints an error if the step count is not a number.',
		'arguments'   : 'standard 4 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'move_nan_stepcount.txt',
	},
	{
		'name'        : 'move step count too low',
		'description' : 'Tests if the move command prints an error if the step count is 0 or less.',
		'arguments'   : 'standard 4 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'move_stepcount_too_low.txt',
	},
	{
		'name'        : 'move step count too high',
		'description' : 'Tests if the move command prints an error if the step count exceeds the last field on a standard boards.',
		'arguments'   : 'standard 4 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'move_stepcount_too_high.txt',
	},
	{
		'name'        : 'move after win',
		'description' : 'Tests if the move command prints an error if it is used after someone won the game.',
		'arguments'   : 'standard 4 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'move_game_won.txt',
	},
	{
		'name'        : 'players argument given',
		'description' : 'Tests if the players command prints an error if an argument is given.',
		'arguments'   : 'standard 4 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'players_argument.txt',
	},
	{
		'name'        : 'rowprint no row number',
		'description' : 'Tests if the rowprint command prints an error if no rownumber is given.',
		'arguments'   : 'standard 4 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'rowprint_no_number.txt',
	},
	{
		'name'        : 'rowprint illegal row number',
		'description' : 'Tests if the rowprint command prints an error if the row number is not a number.',
		'arguments'   : 'standard 4 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'rowprint_nan_number.txt',
	},
	{
		'name'        : 'rowprint row number too low',
		'description' : 'Tests if the rowprint command prints an error if the row number is less than 0.',
		'arguments'   : 'standard 4 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'rowprint_number_too_low.txt',
	},
	{
		'name'        : 'rowprint row number too high',
		'description' : 'Tests if the rowprint command prints an error if the row number exceeds the board size.',
		'arguments'   : 'standard 4 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'rowprint_number_too_high.txt',
	},
	{
		'name'        : 'colprint no col number',
		'description' : 'Tests if the colprint command prints an error if no colnumber is given.',
		'arguments'   : 'standard 4 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'colprint_no_number.txt',
	},
	{
		'name'        : 'colprint illegal col number',
		'description' : 'Tests if the colprint command prints an error if the col number is not a number.',
		'arguments'   : 'standard 4 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'colprint_nan_number.txt',
	},
	{
		'name'        : 'colprint col number too low',
		'description' : 'Tests if the colprint command prints an error if the col number is less than 0.',
		'arguments'   : 'standard 4 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'colprint_number_too_low.txt',
	},
	{
		'name'        : 'colprint col number too high',
		'description' : 'Tests if the colprint command prints an error if the col number exceeds the board size.',
		'arguments'   : 'standard 4 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'colprint_number_too_high.txt',
	},
	{
		'name'        : 'quit argument given',
		'description' : 'Tests if the quit command prints an error if an argument is given.',
		'arguments'   : 'standard 4 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'quit_argument.txt',
	},
]

for test in tests:
	test['protocol'] = 'copy/protocols/commands_error/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
						  description="These test the error handling of all the commands.")
sys.exit(0 if success else 1)
