#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
		'name'        : 'No parameters',
		'description' : 'Tests if the program prints an error message if there are no parameters.',
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Too many parameters',
		'description' : 'Tests if the program prints an error message if there are too many parameters.',
		'arguments'   : 'standard 4 2 input.txt lol',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Board type unkown value',
		'description' : 'Tests if the program prints an error message if the board type is unknown.',
		'arguments'   : 'wrong 4 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Board type missing',
		'description' : 'Tests if the program prints an error message if the board type is missing.',
		'arguments'   : '4 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	
	{
		'name'        : 'Board size too small',
		'description' : 'Tests if the program prints an error message if the board size is too small.',
		'arguments'   : 'standard 2 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Board size not a number',
		'description' : 'Tests if the program prints an error message if the board size is not a number.',
		'arguments'   : 'standard a 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Player number too small',
		'description' : 'Tests if the program prints an error message if the player number is too small.',
		'arguments'   : 'standard 4 1 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Player number too big',
		'description' : 'Tests if the program prints an error message if the player number is too big.',
		'arguments'   : 'standard 10 26 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Player number too big with redirections',
		'description' : 'Tests if the program prints an error message if the player number is too big because of redirections.',
		'arguments'   : 'standard 4 12 input.txt',
		'files': {"input.txt": open('copy/input/too_many_redirections.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Player number too big board size',
		'description' : 'Tests if the program prints an error message if the player number is too big because of board size.',
		'arguments'   : 'standard 4 16 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Player number not a number',
		'description' : 'Tests if the program prints an error message if the player number is not a number.',
		'arguments'   : 'standard 4 a input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Player number or board size missing',
		'description' : 'Tests if the program prints an error message if the player number or board size is missing.',
		'arguments'   : 'standard 4 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
]

for test in tests:
	test['protocol'] = 'copy/protocols/startup_error/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
						  description="These test the error handling of command line parameters.")
sys.exit(0 if success else 1)
