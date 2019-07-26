#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
        'name': "Output check",
        'description': "This tests if there is some output made by the program. If not, the main method isn't doing what it should.",
    }
]

for test in tests:
	pAnalyserList = [
		analysers.ExceptionAnalyser(analysers.NumberOfLinesAnalyser(1, False)),
	]
	
	test['analysers'] = {
        'stdout': analysers.ParallelAnalyser(pAnalyserList),
        'stderr': analysers.ExceptionAnalyser(),
		'exit': analysers.ExpectZeroExitAnalyser()
    }

success = ipo.run(sys.argv[1:], tests, description="This tests if the student made some output with his program.")
sys.exit(0 if success else 1)