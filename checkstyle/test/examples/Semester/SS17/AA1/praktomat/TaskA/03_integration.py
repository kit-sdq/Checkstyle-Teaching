#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
		'name'        : 'Whole game standard board',
		'description' : 'Tests a full game with a standard board.',
		'arguments'   : 'standard 6 4 input.txt',
		'files': {"input.txt": open('copy/input/additional_redirections_standard.txt', 'r').read()},
		'protocol'    : 'whole_game_standard.txt',
	},
	{
		'name'        : 'Whole game torus board',
		'description' : 'Tests a full game with a torus board.',
		'arguments'   : 'torus 5 3 input.txt',
		'files': {"input.txt": open('copy/input/additional_redirections_torus.txt', 'r').read()},
		'protocol'    : 'whole_game_torus.txt',
	},
]

for test in tests:
	test['protocol'] = 'copy/protocols/integration/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
						  description="These are integration tests for full games.")
sys.exit(0 if success else 1)
