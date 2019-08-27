#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
		'name': "Example",
		'description': "Runs the example.txt from the assignment sheet.",
		'arguments': "input.txt waitingarea=fifo",
		'stdout': open('copy/expected/public/example.txt', 'r').read(),
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()}
	}
]

for test in tests:
	test['analysers'] = {
		'stdout': analysers.ExceptionAnalyser(analysers.LineByLineAnalyser(test['stdout'])),
		'stderr': analysers.ExceptionAnalyser()
	}

success = ipo.run(sys.argv[1:], tests,
				  description="These are public tests. Remember, we have got a lot more than just these.")
sys.exit(0 if success else 1)
