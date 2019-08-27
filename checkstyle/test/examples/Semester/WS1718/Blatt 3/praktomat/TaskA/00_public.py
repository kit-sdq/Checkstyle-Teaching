#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
        'name': "Creation",
        'description': "This tests if creation an account and a bank works.",
		'arguments': "public",
		'stdout': """Creating account...
		Initial balance: 0
		Creating bank...
		Done."""
    }
]

for test in tests:
	pAnalyserList = [
		analysers.ExceptionAnalyser(analysers.LineByLineAnalyser(test['stdout'])),
	]
	
	test['analysers'] = {
        'stdout': analysers.ParallelAnalyser(pAnalyserList),
        'stderr': analysers.ExceptionAnalyser(),
		'exit': analysers.ExpectZeroExitAnalyser()
    }

success = ipo.run(sys.argv[1:], tests, description="These are public tests. Remember, we have got a lot more than just these.")
sys.exit(0 if success else 1)