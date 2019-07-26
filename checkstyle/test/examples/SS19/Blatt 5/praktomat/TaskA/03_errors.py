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
		'stdout': """^(Error,.*)$"""
	},
	{
		'name'        : 'Too many parameters',
		'description' : 'Tests if the program prints an error if too many parameters are given.',
		'arguments'   : 'input.cnf a',
		'stdout': """^(Error,.*)$""",
		'files': {"input.cnf": open('copy/input/sheet-example.cnf', 'r').read()},
	},
	{
		'name'        : 'Empty input file',
		'description' : 'Tests if the program prints an error if the input file is empty.',
		'arguments'   : 'input.cnf',
		'stdout': """^(Error,.*)$""",
		'files': {"input.cnf": open('copy/input/error/empty.cnf', 'r').read()},
	},
	{
		'name'        : 'Empty line',
		'description' : 'Tests if the program prints an error if the input file contains an empty line.',
		'arguments'   : 'input.cnf',
		'stdout': """^(Error,.*)$""",
		'files': {"input.cnf": open('copy/input/error/emptyline.cnf', 'r').read()},
	},
	{
		'name'        : 'Illegal character',
		'description' : 'Tests if the program prints an error if the input file contains illegal characters.',
		'arguments'   : 'input.cnf',
		'stdout':"""^(Error,.*)$""",
		'files': {"input.cnf": open('copy/input/error/illegal_character.cnf', 'r').read()},
	},
	{
		'name'        : 'No p line',
		'description' : 'Tests if the program prints an error if the input file contains no p line.',
		'arguments'   : 'input.cnf',
		'stdout': """^(Error,.*)$""",
		'files': {"input.cnf": open('copy/input/error/nopline.cnf', 'r').read()},
	},
	{
		'name'        : 'illegal p line',
		'description' : 'Tests if the program prints an error if the input file contains an illegal p line.',
		'arguments'   : 'input.cnf',
		'stdout':"""^(Error,.*)$""",
		'files': {"input.cnf": open('copy/input/error/illegal_pline.cnf', 'r').read()},
	},
	{
		'name'        : 'nonexisting literal',
		'description' : 'Tests if the program prints an error if the input file contains a nonexiting literal.',
		'arguments'   : 'input.cnf',
		'stdout': """^(Error,.*)$""",
		'files': {"input.cnf": open('copy/input/error/nonexisting_literal.cnf', 'r').read()},
	},
]

for test in tests:
	pAnalyserList = [
			analysers.ExceptionAnalyser(analysers.NumberOfLinesAnalyser(1)),
			analysers.ExceptionAnalyser(analysers.MatchLineAnalyser(test['stdout'])),
	]
	test['analysers'] = {
		'stdout': analysers.ExceptionAnalyser(analysers.ParallelAnalyser(pAnalyserList)),
		'stderr': analysers.ExceptionAnalyser()
	}

success = ipo.run(sys.argv[1:], tests,
						  description="These test correct error handling of command line parameters and the input file.")
sys.exit(0 if success else 1)
