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
		'arguments': "input.txt waitingarea=lifo",
		'stdout': open('copy/expected/private_lifo/example.txt', 'r').read(),
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()}
	},
	{
		'name': "Example complex",
		'description': "Runs the example.txt from the assignment sheet with complex jobs.",
		'arguments': "input.txt waitingarea=lifo",
		'stdout': open('copy/expected/private_lifo/complex.txt', 'r').read(),
		'files': {"input.txt": open('copy/input/example_complex.txt', 'r').read()}
	},
	{
		'name': "Example mixed",
		'description': "Runs the example.txt from the assignment sheet with mixed jobs.",
		'arguments': "input.txt waitingarea=lifo",
		'stdout': open('copy/expected/private_lifo/mixed.txt', 'r').read(),
		'files': {"input.txt": open('copy/input/example_mixed.txt', 'r').read()}
	},
	{
		'name': "Example unordered",
		'description': "Runs the example from the assignment sheet with jobs not ordered by arrival time.",
		'arguments': "input.txt waitingarea=lifo",
		'stdout': open('copy/expected/private_lifo/example.txt', 'r').read(),
		'files': {"input.txt": open('copy/input/example_unordered.txt', 'r').read()}
	},
	{
		'name': "Example Task4 late",
		'description': "Runs the example from the assignment sheet with late Task4 to get a new job just right after the current one has ended.",
		'arguments': "input.txt waitingarea=lifo",
		'stdout': open('copy/expected/private_lifo/late4.txt', 'r').read(),
		'files': {"input.txt": open('copy/input/example_late4.txt', 'r').read()}
	}
]

for test in tests:
	test['analysers'] = {
		'stdout': analysers.ExceptionAnalyser(analysers.LineByLineAnalyser(test['stdout'])),
		'stderr': analysers.ExceptionAnalyser()
	}

success = ipo.run(sys.argv[1:], tests,
				  description="These test correct function of the Last In First Out scheduling strategy.")
sys.exit(0 if success else 1)
