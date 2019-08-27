#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
        'name': "Main method test",
        'description': "Checks if the lines are matching the specification.",
		'stdout': """(.* .* \d \d|.* \d|.* .* \d)"""
    }
]

for test in tests:
	pAnalyserList = [
		analysers.ExceptionAnalyser(analysers.MatchLineAnalyser(test['stdout'])),
		analysers.NumberOfLinesAnalyser(4, False)
	]
	
	test['analysers'] = {
        'stdout': analysers.ParallelAnalyser(pAnalyserList),
        'stderr': analysers.ExceptionAnalyser(),
		'exit': analysers.ExpectZeroExitAnalyser()
    }

success = ipo.run(sys.argv[1:], tests, description="These are public tests. Remember, we have got a lot more than just these.")
sys.exit(0 if success else 1)