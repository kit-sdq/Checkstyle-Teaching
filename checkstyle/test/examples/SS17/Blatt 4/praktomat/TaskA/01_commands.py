#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
		'name'        : 'Move Test',
		'description' : 'Tests if the move command works correctly with a black and white game.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'command_move.txt',
	},
	{
		'name'        : 'Move Test Colored',
		'description' : 'Tests if the move command works correctly with a colored game.',
		'arguments'   : 'input.txt rule=270-90-315-45-90',
		'files': {"input.txt": open('copy/input/example_colored.txt', 'r').read()},
		'protocol'    : 'command_move_colored.txt',
	},
	{
		'name'        : 'Move Test Multiple Ants',
		'description' : 'Tests if the move command works correctly with a black and white game and multiple ants. Tests leaving aswell.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example_multiple.txt', 'r').read()},
		'protocol'    : 'command_move_multiple.txt',
	},
	{
		'name'        : 'Move Test Exit',
		'description' : 'Tests if the move command exits the game after all ants left.',
		'arguments'   : 'input.txt rule=270-90-315-45-90',
		'files': {"input.txt": open('copy/input/example_colored.txt', 'r').read()},
		'protocol'    : 'command_move_exit.txt',
	},
	{
		'name'        : 'Position Test',
		'description' : 'Tests if the position command works correctly.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example_multiple.txt', 'r').read()},
		'protocol'    : 'command_position.txt',
	},
	{
		'name'        : 'Field Test',
		'description' : 'Tests if the field command works correctly.',
		'arguments'   : 'input.txt rule=270-90-315-45-90',
		'files': {"input.txt": open('copy/input/example_colored_multiple.txt', 'r').read()},
		'protocol'    : 'command_field.txt',
	},
	{
		'name'        : 'Direction Test',
		'description' : 'Tests if the direction command works correctly.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example_multiple.txt', 'r').read()},
		'protocol'    : 'command_direction.txt',
	},
	{
		'name'        : 'Direction with Move Test',
		'description' : 'Tests if the direction command works correctly after moves.',
		'arguments'   : 'input.txt rule=270-90-315-45-90',
		'files': {"input.txt": open('copy/input/example_colored.txt', 'r').read()},
		'protocol'    : 'command_direction_move.txt',
	},
	{
		'name'        : 'Ant Test',
		'description' : 'Tests if the ant command works correctly.',
		'arguments'   : 'input.txt rule=270-90-315-45-90',
		'files': {"input.txt": open('copy/input/moarants.txt', 'r').read()},
		'protocol'    : 'command_ant.txt',
	},
	{
		'name'        : 'Create Test',
		'description' : 'Tests if the create command works correctly.',
		'arguments'   : 'input.txt rule=270-90-315-45-90',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'command_create.txt',
	},
	{
		'name'        : 'Create and Ant Test',
		'description' : 'Tests if the create and ant commands work correctly together.',
		'arguments'   : 'input.txt rule=270-90-315-45-90',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'command_create_ant.txt',
	},
	{
		'name'        : 'Escape Test',
		'description' : 'Tests if the escape command works correctly.',
		'arguments'   : 'input.txt rule=270-90-315-45-90',
		'files': {"input.txt": open('copy/input/example_multiple.txt', 'r').read()},
		'protocol'    : 'command_escape.txt',
	},
	{
		'name'        : 'Escape Test Exit',
		'description' : 'Tests if the escape command works correctly and exitting works.',
		'arguments'   : 'input.txt rule=270-90-315-45-90',
		'files': {"input.txt": open('copy/input/example_multiple.txt', 'r').read()},
		'protocol'    : 'command_escape_exit.txt',
	}
]

for test in tests:
	test['protocol'] = 'copy/protocols/commands/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
						  description="These test all the commands for correct functionality.")
sys.exit(0 if success else 1)
