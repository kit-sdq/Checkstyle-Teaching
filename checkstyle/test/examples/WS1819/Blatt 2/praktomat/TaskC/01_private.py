#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': "Correct calculation with R = 7, r = 3, h = 4",
        'description': "Checks if the result ist correct for the values R = 7, r = 3, h = 4.",
        "arguments": "7 3 4",
        'stdout': "330\.91442.*;359\.92769.*;177\.71531.*;5\.65685.*"
    }
]

for test in tests:
	pAnalyserList = [
		analysers.ExceptionAnalyser(analysers.NumberOfLinesAnalyser(1)),
		analysers.ExceptionAnalyser(analysers.MatchLineAnalyser(test['stdout'], message="Unexpected line, at least one of the results is either wrong or not exact enough. Expected 330.91442xx;359.92769xx;177.71531xx;5.65685xx")),
	]
	
	test['analysers'] = {
        'stdout': analysers.ParallelAnalyser(pAnalyserList),
        'stderr': analysers.ExceptionAnalyser(),
		'exit': analysers.ExpectZeroExitAnalyser()
    }

success = ipo.run(sys.argv[1:], tests,
                  description="This is a private test for easy correction.")
sys.exit(0 if success else 1)
