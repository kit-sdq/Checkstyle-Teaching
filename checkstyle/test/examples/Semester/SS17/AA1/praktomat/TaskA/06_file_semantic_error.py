#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
		'name'        : 'Source X coordinate too low',
		'description' : 'Tests if the program prints an error message if the X coordinate of a source field is too low.',
		'arguments'   : 'standard 6 2 input.txt',
		'files': {"input.txt": open('copy/input/source_x_low.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Source X coordinate too high',
		'description' : 'Tests if the program prints an error message if the X coordinate of a source field is too high.',
		'arguments'   : 'standard 6 2 input.txt',
		'files': {"input.txt": open('copy/input/source_x_high.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Source Y coordinate too low',
		'description' : 'Tests if the program prints an error message if the Y coordinate of a source field is too low.',
		'arguments'   : 'standard 6 2 input.txt',
		'files': {"input.txt": open('copy/input/source_y_low.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Source Y coordinate too high',
		'description' : 'Tests if the program prints an error message if the Y coordinate of a source field is too high.',
		'arguments'   : 'standard 6 2 input.txt',
		'files': {"input.txt": open('copy/input/source_y_high.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Target X coordinate too low',
		'description' : 'Tests if the program prints an error message if the X coordinate of a target field is too low.',
		'arguments'   : 'standard 6 2 input.txt',
		'files': {"input.txt": open('copy/input/target_x_low.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Target X coordinate too high',
		'description' : 'Tests if the program prints an error message if the X coordinate of a target field is too high.',
		'arguments'   : 'standard 6 2 input.txt',
		'files': {"input.txt": open('copy/input/target_x_high.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Target Y coordinate too low',
		'description' : 'Tests if the program prints an error message if the Y coordinate of a target field is too low.',
		'arguments'   : 'standard 6 2 input.txt',
		'files': {"input.txt": open('copy/input/target_y_low.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Target Y coordinate too high',
		'description' : 'Tests if the program prints an error message if the Y coordinate of a target field is too high.',
		'arguments'   : 'standard 6 2 input.txt',
		'files': {"input.txt": open('copy/input/target_y_high.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Double redirection',
		'description' : 'Tests if the program prints an error message if there are multiple redirections from the same field.',
		'arguments'   : 'standard 6 2 input.txt',
		'files': {"input.txt": open('copy/input/double_redirection.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Redirection to first field',
		'description' : 'Tests if the program prints an error message if there is a redirection to the first field.',
		'arguments'   : 'standard 6 2 input.txt',
		'files': {"input.txt": open('copy/input/redirection_to_first.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Redirection to last field',
		'description' : 'Tests if the program prints an error message if there is a redirection to the last field.',
		'arguments'   : 'standard 6 2 input.txt',
		'files': {"input.txt": open('copy/input/redirection_to_last.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Redirection from first field',
		'description' : 'Tests if the program prints an error message if there is a redirection from the first field.',
		'arguments'   : 'standard 6 2 input.txt',
		'files': {"input.txt": open('copy/input/redirection_from_first.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Redirection from last field',
		'description' : 'Tests if the program prints an error message if there is a redirection from the last field.',
		'arguments'   : 'standard 6 2 input.txt',
		'files': {"input.txt": open('copy/input/redirection_from_last.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Chain redirection',
		'description' : 'Tests if the program prints an error message if there are chained redirections.',
		'arguments'   : 'standard 6 2 input.txt',
		'files': {"input.txt": open('copy/input/chain_redirection.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
]

for test in tests:
	test['protocol'] = 'copy/protocols/startup_error/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
						  description="These test correct error handling of semantic errors in the input file.")
sys.exit(0 if success else 1)