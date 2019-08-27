#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
		'name'        : 'Public Test',
		'description' : 'Tests if the example from the assignment sheet works roughly correctly.',
		'arguments'   : 'input.cnf',
		'stdout': """^(Processing Bucket (1|2|3|4|5))|({.*})|(SAT)$""",
		'files': {"input.cnf": open('copy/input/sheet-example.cnf', 'r').read()}
	},
]

for test in tests:
	pAnalyserList = [
			analysers.ExceptionAnalyser(analysers.NumberOfLinesAnalyser(9)),
			analysers.ExceptionAnalyser(analysers.MatchLineAnalyser(test['stdout'])),
	]
	test['analysers'] = {
		'stdout': analysers.ExceptionAnalyser(analysers.ParallelAnalyser(pAnalyserList)),
		'stderr': analysers.ExceptionAnalyser()
	}

success = ipo.run(sys.argv[1:], tests,
						  description="These are public tests. Remember, we have got a lot more than just these.")
sys.exit(0 if success else 1)
