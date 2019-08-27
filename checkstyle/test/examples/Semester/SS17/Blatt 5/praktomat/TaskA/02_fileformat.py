#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
		'name'        : 'random casing',
		'description' : 'Tests if the program can read a file with arbitrary casing and different casings for already existing nodes.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/video_random_casing.txt', 'r').read()},
		'protocol'    : 'fileformat.txt',
	},
	{
		'name'        : 'random spaces',
		'description' : 'Tests if the program can read a file with arbitrary amount of spaces at allowed positions.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/video_random_spaces.txt', 'r').read()},
		'protocol'    : 'fileformat.txt',
	},
	{
		'name'        : 'both edges given',
		'description' : 'Tests if the program can read a file with some inverse edges given aswell.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/video_with_inverse.txt', 'r').read()},
		'protocol'    : 'fileformat.txt',
	},
]

for test in tests:
	test['protocol'] = 'copy/protocols/fileformat/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
						  description="These test allowed but strange file formatting. Not crashing is already pretty good.")
sys.exit(0 if success else 1)
