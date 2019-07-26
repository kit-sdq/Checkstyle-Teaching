#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
		'name': "Print format",
		'description': "Checks if the creation works for more complex arrays.",
		'arguments': "FUNCTIONS CREATE",
		'stdout': open('copy/expected/functions/create.txt', 'r').read()
	},
	{
		'name': "Minimum",
		'description': "Checks if the minimum is correctly implemented.",
		'arguments': "FUNCTIONS MIN",
		'stdout': open('copy/expected/functions/min.txt', 'r').read()
	},
	{
		'name': "Maximum",
		'description': "Checks if the maximum is correctly implemented.",
		'arguments': "FUNCTIONS MAX",
		'stdout': open('copy/expected/functions/max.txt', 'r').read()
	},
	{
		'name': "Insertion",
		'description': "Checks if the insertion of new values is correctly implemented.",
		'arguments': "FUNCTIONS INSERT",
		'stdout': open('copy/expected/functions/insert.txt', 'r').read()
	},
	{
		'name': "Remove",
		'description': "Checks if the removal of values is correctly implemented.",
		'arguments': "FUNCTIONS REMOVE",
		'stdout': open('copy/expected/functions/remove.txt', 'r').read()
	},
	{
		'name': "IndexOf",
		'description': "Checks if the indexOf method is correctly implemented.",
		'arguments': "FUNCTIONS INDEXOF",
		'stdout': open('copy/expected/functions/indexof.txt', 'r').read()
	},
	{
		'name': "CountNumbers",
		'description': "Checks if the countNumbers method is correctly implemented.",
		'arguments': "FUNCTIONS COUNTNUMBERS",
		'stdout': open('copy/expected/functions/countnumbers.txt', 'r').read()
	},
	{
		'name': "Swap",
		'description': "Checks if the swap method is correctly implemented.",
		'arguments': "FUNCTIONS SWAP",
		'stdout': open('copy/expected/functions/swap.txt', 'r').read()
	},
	{
		'name': "ToSet",
		'description': "Checks if the toSet method is correctly implemented.",
		'arguments': "FUNCTIONS TOSET",
		'stdout': open('copy/expected/functions/toset.txt', 'r').read()
	},
	{
		'name': "Equals",
		'description': "Checks if the equals method is correctly implemented.",
		'arguments': "FUNCTIONS EQUALS",
		'stdout': open('copy/expected/functions/equals.txt', 'r').read()
	}
]

for test in tests:
	test['analysers'] = {
		'stdout': analysers.ExceptionAnalyser(analysers.LineByLineAnalyser(test['stdout'])),
		'stderr': analysers.ExceptionAnalyser()
	}

success = ipo.run(sys.argv[1:], tests,
				  description="This private test tests all functionality methods. A good rating requires all these tests to be passed.",
				  main="Wrapper")
sys.exit(0 if success else 1)
