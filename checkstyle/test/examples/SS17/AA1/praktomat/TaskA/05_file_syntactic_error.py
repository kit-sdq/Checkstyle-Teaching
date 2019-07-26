#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
		'name'        : 'Extra spaces',
		'description' : 'Tests if the program prints an error message if there are extra spaces in the file.',
		'arguments'   : 'standard 6 2 input.txt',
		'files': {"input.txt": open('copy/input/extra_spaces.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Unexpected symbols',
		'description' : 'Tests if the program prints an error message if there are unexpected in the file.',
		'arguments'   : 'standard 6 2 input.txt',
		'files': {"input.txt": open('copy/input/unexpected_symbols.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Empty line',
		'description' : 'Tests if the program prints an error message if there are empty lines in the file.',
		'arguments'   : 'standard 6 2 input.txt',
		'files': {"input.txt": open('copy/input/empty_lines.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
]

for test in tests:
	test['protocol'] = 'copy/protocols/startup_error/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
						  description="These test correct error handling of syntactic errors in the input file.")
sys.exit(0 if success else 1)