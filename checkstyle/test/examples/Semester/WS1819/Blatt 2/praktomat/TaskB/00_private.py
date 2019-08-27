#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
        'name': "Task check",
        'description': "This tests if the task is correctly solved for grading convenience.",
		'stdout': """^(Aktuelles Datum: 6\.12\.2018)$"""
    }
]

for test in tests:
	pAnalyserList = [
		analysers.ExceptionAnalyser(analysers.NumberOfLinesAnalyser(1)),
		analysers.ExceptionAnalyser(analysers.MatchLineAnalyser(test['stdout'])),
	]
	
	test['analysers'] = {
        'stdout': analysers.ParallelAnalyser(pAnalyserList),
        'stderr': analysers.ExceptionAnalyser(),
		'exit': analysers.ExpectZeroExitAnalyser()
    }

success = ipo.run(sys.argv[1:], tests, description="This tests if the task is correctly solved for grading convenience.")
sys.exit(0 if success else 1)
