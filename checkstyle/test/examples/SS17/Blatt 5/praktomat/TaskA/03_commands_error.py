#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
		'name'        : 'nodes with argument',
		'description' : 'Tests if the nodes command prints an error if an argument is given.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'nodes_with_argument.txt',
	},
	{
		'name'        : 'edges with argument',
		'description' : 'Tests if the nodes command prints an error if an argument is given.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'nodes_with_argument.txt',
	},
	{
		'name'        : 'recommend no arguments',
		'description' : 'Tests if the recommend command prints an error if no arguments are given.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'recommend_no_args.txt',
	},
	{
		'name'        : 'recommend only strategy',
		'description' : 'Tests if the recommend command prints an error if only the strategy is given.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'recommend_only_strategy.txt',
	},
	{
		'name'        : 'recommend only id',
		'description' : 'Tests if the recommend command prints an error if only the id is given.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'recommend_only_id.txt',
	},
	{
		'name'        : 'recommend arguments swapped',
		'description' : 'Tests if the recommend command prints an error if the arguments are swapped.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'recommend_args_swapped.txt',
	},
	{
		'name'        : 'recommend strategy unknown',
		'description' : 'Tests if the recommend command prints an error if the strategy is unknown.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'recommend_strategy_unknown.txt',
	},
	{
		'name'        : 'recommend id unknown',
		'description' : 'Tests if the recommend command prints an error if the id is unknown.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'recommend_id_unknown.txt',
	},
	{
		'name'        : 'recommend both unknown',
		'description' : 'Tests if the recommend command prints an error if both arguments are unknown.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'recommend_both_unknown.txt',
	},
	{
		'name'        : 'recommend id illegal',
		'description' : 'Tests if the recommend command prints an error if the id is an illegal character.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'recommend_id_illegal.txt',
	},
	{
		'name'        : 'export with argument',
		'description' : 'Tests if the export command prints an error if an argument is given.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'export_with_argument.txt',
	},
	{
		'name'        : 'quit with argument',
		'description' : 'Tests if the quit command prints an error if an argument is given.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'quit_with_argument.txt',
	},
	{
		'name'        : 'Nonexistent command',
		'description' : 'Tests if the program prints an error if the command is unknown.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'nonexistent_command.txt',
	}
]

for test in tests:
	test['protocol'] = 'copy/protocols/commands_error/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
						  description="These test the error handling of all the commands.")
sys.exit(0 if success else 1)
