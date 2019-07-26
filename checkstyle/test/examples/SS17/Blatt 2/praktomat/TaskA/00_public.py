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
		'description': "Checks if the print format is correctly implemented.",
		'arguments': "PUBLIC PRINT",
		'stdout': open('copy/expected/public/print.txt', 'r').read()
	},
	{
		'name': "Creation",
		'description': "Checks if the creation is correctly implemented.",
		'arguments': "PUBLIC CREATE",
		'stdout': open('copy/expected/public/create.txt', 'r').read()
	}
]

for test in tests:
	test['analysers'] = {
		'stdout': analysers.ExceptionAnalyser(analysers.LineByLineAnalyser(test['stdout'])),
		'stderr': analysers.ExceptionAnalyser()
	}

success = ipo.run(sys.argv[1:], tests,
				  description="These are public tests. Remember, we have got a lot more than just these.",
				  main="Wrapper")
sys.exit(0 if success else 1)
