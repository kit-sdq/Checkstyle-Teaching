#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
		'name'        : 'No parameters',
		'description' : 'Tests if the program prints an error if no parameters are given.',
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Too many parameters',
		'description' : 'Tests if the program prints an error if too many parameters are given.',
		'arguments'   : 'input.txt rule=90-45-45-270-45 a',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Second parameter unknown',
		'description' : 'Tests if the program prints an error if a parameter is unknown.',
		'arguments'   : 'input.txt b',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Rule no value',
		'description' : 'Tests if the program prints an error if the rule is missing the value.',
		'arguments'   : 'input.txt rule=',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Rule illegal value',
		'description' : 'Tests if the program prints an error if the rule value is illegal.',
		'arguments'   : 'input.txt rule=a',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Rule illegal character',
		'description' : 'Tests if the program prints an error if the rule value contains illegal characters.',
		'arguments'   : 'input.txt rule=b0-45-a-270-45',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Rule illegal number',
		'description' : 'Tests if the program prints an error if the rule value contains an illegal number.',
		'arguments'   : 'input.txt rule=90-45-180-270-45',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Rule illegal seperator',
		'description' : 'Tests if the program prints an error if the rule value contains illegal seperators.',
		'arguments'   : 'input.txt rule=90,45;180-270-45',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Empty input file',
		'description' : 'Tests if the program prints an error if the input file is empty.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/empty.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Empty line',
		'description' : 'Tests if the program prints an error if the input file contains an empty line.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/emptyline.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Nonexisting color',
		'description' : 'Tests if the program prints an error if the input file contains nonexisting colors.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/nonexistent_color.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Illegal character',
		'description' : 'Tests if the program prints an error if the input file contains illegal characters.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/illegal_character.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Spaces between characters',
		'description' : 'Tests if the program prints an error if the input file contains spaces between characters.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/spaces.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Different line length',
		'description' : 'Tests if the program prints an error if the input file contains lines with different lengths.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/different_line_lengths.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Double ant',
		'description' : 'Tests if the program prints an error if the input file contains two ants with the same name.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/double_ant.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Double ant different casing',
		'description' : 'Tests if the program prints an error if the input file contains two ants with the same name.',
		'arguments'   : 'input.txt rule=90-45-45-270-45',
		'files': {"input.txt": open('copy/input/double_ant_casing.txt', 'r').read()},
		'protocol'    : 'error.txt',
	}
]

for test in tests:
	test['protocol'] = 'copy/protocols/other_errors/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
						  description="These test correct error handling of command line parameters and the input file.")
sys.exit(0 if success else 1)