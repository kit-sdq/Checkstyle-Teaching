#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
		'name'        : 'Implicit default rule',
		'description' : 'Tests if the implicit default rule works.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/example_colored.txt', 'r').read()},
		'protocol'    : 'command_move_colored.txt',
	},
	{
		'name'        : 'Instant Exit',
		'description' : 'Tests if exiting after just one move works correctly.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example_south.txt', 'r').read()},
		'protocol'    : 'instant_exit.txt',
	},
	{
		'name'        : 'Example with default rule',
		'description' : 'Tests if running the example with the default rule works correctly.',
		'arguments'   : 'input.txt rule=270-90-315-45-90',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'example_default_rule.txt',
	},
	{
		'name'        : 'Non quadratic board',
		'description' : 'Tests if running on a non quadratic board works correctly.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/example_non_quadratic.txt', 'r').read()},
		'protocol'    : 'non_quadratic.txt',
	},
	{
		'name'        : 'Bigger board',
		'description' : 'Tests if running on a bigger board visiting all colors at least once works correctly.',
		'arguments'   : 'input.txt rule=45-90-270-315-45',
		'files': {"input.txt": open('copy/input/6x6.txt', 'r').read()},
		'protocol'    : 'allcolors.txt',
	},
	{
		'name'        : 'Wikipedia Example',
		'description' : 'Tests if the example from wikipedia works. Many steps, so timeouts are possible!',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/wiki.txt', 'r').read()},
		'protocol'    : 'wiki.txt',
		'timeout'	  : '10',
	},
]

for test in tests:
	test['protocol'] = 'copy/protocols/interaction/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
						  description="These test more complex interactions and edge cases.")
sys.exit(0 if success else 1)
