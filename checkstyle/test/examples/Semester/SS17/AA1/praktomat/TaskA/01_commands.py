#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
		'name'        : 'Rowprint start of game',
		'description' : 'Tests if the rowprint command works correctly directly after starting a game.',
		'arguments'   : 'standard 4 2 input.txt',
		'files': {"input.txt": open('copy/input/commands_test.txt', 'r').read()},
		'protocol'    : 'rowprint_start.txt',
	},
	{
		'name'        : 'Rowprint moving from start',
		'description' : 'Tests if the rowprint command works correctly with players moving from the start gradually.',
		'arguments'   : 'standard 4 2 input.txt',
		'files': {"input.txt": open('copy/input/commands_test.txt', 'r').read()},
		'protocol'    : 'rowprint_move.txt',
	},
	{
		'name'        : 'Rowprint after kick',
		'description' : 'Tests if the rowprint command works correctly after kicking a player.',
		'arguments'   : 'standard 4 2 input.txt',
		'files': {"input.txt": open('copy/input/commands_test.txt', 'r').read()},
		'protocol'    : 'rowprint_kick.txt',
	},
	{
		'name'        : 'Rowprint after win',
		'description' : 'Tests if the rowprint command works correctly after someone won the game.',
		'arguments'   : 'standard 4 2 input.txt',
		'files': {"input.txt": open('copy/input/commands_test.txt', 'r').read()},
		'protocol'    : 'rowprint_win.txt',
	},
	{
		'name'        : 'Colprint start of game',
		'description' : 'Tests if the colprint command works correctly directly after starting a game.',
		'arguments'   : 'standard 4 2 input.txt',
		'files': {"input.txt": open('copy/input/commands_test.txt', 'r').read()},
		'protocol'    : 'colprint_start.txt',
	},
	{
		'name'        : 'Colprint moving from start',
		'description' : 'Tests if the colprint command works correctly with players moving from the start gradually.',
		'arguments'   : 'standard 4 2 input.txt',
		'files': {"input.txt": open('copy/input/commands_test.txt', 'r').read()},
		'protocol'    : 'colprint_move.txt',
	},
	{
		'name'        : 'Colprint after kick',
		'description' : 'Tests if the colprint command works correctly after kicking a player.',
		'arguments'   : 'standard 4 2 input.txt',
		'files': {"input.txt": open('copy/input/commands_test.txt', 'r').read()},
		'protocol'    : 'colprint_kick.txt',
	},
	{
		'name'        : 'Colprint after win',
		'description' : 'Tests if the colprint command works correctly after someone won the game.',
		'arguments'   : 'standard 4 2 input.txt',
		'files': {"input.txt": open('copy/input/commands_test.txt', 'r').read()},
		'protocol'    : 'colprint_win.txt',
	},
	{
		'name'        : 'Players start of game',
		'description' : 'Tests if the players command works correctly directly after starting a game.',
		'arguments'   : 'standard 4 2 input.txt',
		'files': {"input.txt": open('copy/input/commands_test.txt', 'r').read()},
		'protocol'    : 'players_start.txt',
	},
	{
		'name'        : 'Players moving from start',
		'description' : 'Tests if the players command works correctly with players moving from the start gradually.',
		'arguments'   : 'standard 4 2 input.txt',
		'files': {"input.txt": open('copy/input/commands_test.txt', 'r').read()},
		'protocol'    : 'players_move.txt',
	},
	{
		'name'        : 'Players after kick',
		'description' : 'Tests if the players command works correctly after kicking a player.',
		'arguments'   : 'standard 4 2 input.txt',
		'files': {"input.txt": open('copy/input/commands_test.txt', 'r').read()},
		'protocol'    : 'players_kick.txt',
	},
	{
		'name'        : 'Players after win',
		'description' : 'Tests if the players command works correctly after someone won the game.',
		'arguments'   : 'standard 4 2 input.txt',
		'files': {"input.txt": open('copy/input/commands_test.txt', 'r').read()},
		'protocol'    : 'players_win.txt',
	}
]

for test in tests:
	test['protocol'] = 'copy/protocols/commands/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
						  description="These test all commands except move for correct functionality. The move command needs to work somewhat correctly for these to pass!")
sys.exit(0 if success else 1)
