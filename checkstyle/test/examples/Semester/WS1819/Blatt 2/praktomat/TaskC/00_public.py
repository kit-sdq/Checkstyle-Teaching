#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': "Correct calculation with R = 2, r = 1, h = 1",
        'description': "Checks if the result ist correct for the values R = 2, r = 1, h = 1.",
        "arguments": "2 1 1",
        'stdout': "7\.33038.*;29\.03661.*;13\.32864.*;1\.41421.*"
    }
]


for test in tests:
	pAnalyserList = [
		analysers.ExceptionAnalyser(analysers.NumberOfLinesAnalyser(1)),
		analysers.ExceptionAnalyser(analysers.MatchLineAnalyser(test['stdout'], message="Unexpected line, at least one of the results is either wrong or not exact enough. Expected 7.33038xx;29.03661xx;13.32864xx;1.41421xx")),
	]
	
	test['analysers'] = {
        'stdout': analysers.ParallelAnalyser(pAnalyserList),
        'stderr': analysers.ExceptionAnalyser(),
		'exit': analysers.ExpectZeroExitAnalyser()
    }

success = ipo.run(sys.argv[1:], tests,
                  description="These are public tests. Remember, we have got a lot more than just these.")
sys.exit(0 if success else 1)
