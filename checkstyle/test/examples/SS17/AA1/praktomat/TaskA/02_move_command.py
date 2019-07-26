#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
		'name'        : 'Move inside row',
		'description' : 'Tests if the move command works correctly without leaving a row.',
		'arguments'   : 'standard 6 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'inside_row.txt',
	},
	{
		'name'        : 'Move to last field of row',
		'description' : 'Tests if the move command doesn\'t leave the row when moving on the last field of the row.',
		'arguments'   : 'standard 6 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'to_last.txt',
	},
	{
		'name'        : 'Move to next row',
		'description' : 'Tests if the move command works correctly when changing the row.',
		'arguments'   : 'standard 6 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'change_row.txt',
	},
	{
		'name'        : 'Direction changing in second row',
		'description' : 'Tests if the move command changes directions correctly in the second row.',
		'arguments'   : 'standard 6 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'direction_second.txt',
	},
	{
		'name'        : 'Skip rows',
		'description' : 'Tests if the move command works correctly when jumping rows.',
		'arguments'   : 'standard 6 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'skip_rows.txt',
	},
	{
		'name'        : 'Move to last row even board size',
		'description' : 'Tests if the move command switches to the correct field when moving to the last row of an evenly sized board.',
		'arguments'   : 'standard 6 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'to_last_even.txt',
	},
	{
		'name'        : 'Direction last row even board size',
		'description' : 'Tests if the move command uses the correct direction in the last row of an evenly sized board.',
		'arguments'   : 'standard 6 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'direction_last_even.txt',
	},
	{
		'name'        : 'Move to last row odd board size',
		'description' : 'Tests if the move command switches to the correct field when moving to the last row of an oddly sized board.',
		'arguments'   : 'standard 5 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'to_last_odd.txt',
	},
	{
		'name'        : 'Direction last row odd board size',
		'description' : 'Tests if the move command uses the correct direction in the last row of an oddly sized board.',
		'arguments'   : 'standard 5 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'direction_last_odd.txt',
	},
	{
		'name'        : 'Win game standard board',
		'description' : 'Tests if the move command wins correctly on a standard board.',
		'arguments'   : 'standard 6 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'win_standard.txt',
	},
	{
		'name'        : 'Step count too high standard board',
		'description' : 'Tests if the move command proceeds to the next player correctly if the step count was too high on a standard board.',
		'arguments'   : 'standard 6 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'stepcount_too_high.txt',
	},
	{
		'name'        : 'Win game torus board',
		'description' : 'Tests if the move command wins correctly on a torus board.',
		'arguments'   : 'torus 6 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'win_torus.txt',
	},
	{
		'name'        : 'Skip last field torus board',
		'description' : 'Tests if the move command skips to the correct field on a torus board.',
		'arguments'   : 'torus 6 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'skip_torus.txt',
	},
	{
		'name'        : 'Win game with skip torus board',
		'description' : 'Tests if the move command wins correctly on a torus board directly after skipping over the last field.',
		'arguments'   : 'torus 6 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'win_skip_torus.txt',
	},
	{
		'name'        : 'Redirection',
		'description' : 'Tests if the move command redirects correctly when stepping on a redirection field.',
		'arguments'   : 'torus 6 2 input.txt',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'redirect.txt',
	},
	{
		'name'        : 'Kick player',
		'description' : 'Tests if the move command kicks the occupying player back to start when stepping on the occupied field.',
		'arguments'   : 'torus 6 2 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'kick.txt',
	},
	{
		'name'        : 'Move rotation more than 2 players',
		'description' : 'Tests if the move command switches players correctly for more than 2 players.',
		'arguments'   : 'torus 6 4 input.txt',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'multiple_players.txt',
	},
]

for test in tests:
	test['protocol'] = 'copy/protocols/move_command/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
						  description="These test the move command for correct functionality. The rowprint and colprint command need to work correctly for the tests to pass!")
sys.exit(0 if success else 1)
