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
		'stdout': """^(332640|(5 4 3 2 1 0)|(1 3 5 7 9 11 13)|(1585|1585\.0))$"""
    }
]

for test in tests:
	pAnalyserList = [
		analysers.ExceptionAnalyser(analysers.MatchLineAnalyser(test['stdout'])),
	]
	
	test['analysers'] = {
        'stdout': analysers.ParallelAnalyser(pAnalyserList),
        'stderr': analysers.ExceptionAnalyser(),
		'exit': analysers.ExpectZeroExitAnalyser()
    }

success = ipo.run(sys.argv[1:], tests, description="This tests if the task is correctly solved for grading convenience.")
sys.exit(0 if success else 1)