#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
		'name'        : 'Move no value',
		'description' : 'Tests if the move command prints an error if no value is given.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'move_no_value.txt',
	},
	{
		'name'        : 'Move negative value',
		'description' : 'Tests if the move command prints an error if a negative value is given.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'move_negative_value.txt',
	},
	{
		'name'        : 'Move no value',
		'description' : 'Tests if the move command prints an error if NaN value is given.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'move_nan_value.txt',
	},
	{
		'name'        : 'Print with argument',
		'description' : 'Tests if the print command prints an error if an argument is given.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'print_with_argument.txt',
	},
	{
		'name'        : 'Position no ant',
		'description' : 'Tests if the position command prints an error if no ant is given.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'position_no_ant.txt',
	},
	{
		'name'        : 'Position illegal character',
		'description' : 'Tests if the position command prints an error if the ant is a illegal character.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'position_illegal_ant.txt',
	},
	{
		'name'        : 'Position nonexistent ant',
		'description' : 'Tests if the position command prints an error if the ant is nonexistent given.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'position_nonexistent_ant.txt',
	},
	{
		'name'        : 'Field no value',
		'description' : 'Tests if the field command prints an error if no value is given.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'field_no_value.txt',
	},
	{
		'name'        : 'Field missing value',
		'description' : 'Tests if the field command prints an error if a coordiante is missing.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'field_missing_value.txt',
	},
	{
		'name'        : 'Field illegal character',
		'description' : 'Tests if the field command prints an error if the value contains illegal characters.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'field_illegal_value.txt',
	},
	{
		'name'        : 'Field wrong seperator',
		'description' : 'Tests if the field command prints an error if the seperator is wrong.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'field_wrong_seperator.txt',
	},
	{
		'name'        : 'Field nonexistent cell',
		'description' : 'Tests if the field command prints an error if the given cell is nonexistent.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'field_nonexistent_value.txt',
	},
	{
		'name'        : 'Direction no ant',
		'description' : 'Tests if the direction command prints an error if no ant is given.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'direction_no_ant.txt',
	},
	{
		'name'        : 'Direction illegal character',
		'description' : 'Tests if the direction command prints an error if the ant is a illegal character.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'direction_illegal_ant.txt',
	},
	{
		'name'        : 'Direction nonexistent ant',
		'description' : 'Tests if the direction command prints an error if the ant is nonexistent given.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'direction_nonexistent_ant.txt',
	},
	{
		'name'        : 'Ant with argument',
		'description' : 'Tests if the ant command prints an error if an argument is given.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'ant_with_argument.txt',
	},
	{
		'name'        : 'Create no value',
		'description' : 'Tests if the create command prints an error if no value is given.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'create_no_value.txt',
	},
	{
		'name'        : 'Create missing value',
		'description' : 'Tests if the create command prints an error if a coordiante is missing.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'create_missing_value.txt',
	},
	{
		'name'        : 'Create illegal character',
		'description' : 'Tests if the create command prints an error if the value contains illegal characters.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'create_illegal_value.txt',
	},
	{
		'name'        : 'Create wrong seperator',
		'description' : 'Tests if the create command prints an error if the seperator is wrong.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'create_wrong_seperator.txt',
	},
	{
		'name'        : 'Create nonexistent cell',
		'description' : 'Tests if the create command prints an error if the given cell is nonexistent.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'create_nonexistent_value.txt',
	},
	{
		'name'        : 'Create existing ant',
		'description' : 'Tests if the create command prints an error if the given ant already exists.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'create_existing_ant.txt',
	},
	{
		'name'        : 'Create occupied cell',
		'description' : 'Tests if the create command prints an error if the given cell is occupied.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'create_occupied_cell.txt',
	},
	{
		'name'        : 'Escape no ant',
		'description' : 'Tests if the escape command prints an error if no ant is given.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'escape_no_ant.txt',
	},
	{
		'name'        : 'Escape illegal character',
		'description' : 'Tests if the escape command prints an error if the ant is a illegal character.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'escape_illegal_ant.txt',
	},
	{
		'name'        : 'Escape nonexistent ant',
		'description' : 'Tests if the escape command prints an error if the ant is nonexistent given.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'escape_nonexistent_ant.txt',
	},
	{
		'name'        : 'Quit with argument',
		'description' : 'Tests if the quit command prints an error if an argument is given.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'quit_with_argument.txt',
	},
	{
		'name'        : 'Nonexistent command',
		'description' : 'Tests if the program prints an error if the command is unknown.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'nonexistent_command.txt',
	},
]

for test in tests:
	test['protocol'] = 'copy/protocols/commands_error/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
						  description="These test the error handling of all the commands.")
sys.exit(0 if success else 1)
