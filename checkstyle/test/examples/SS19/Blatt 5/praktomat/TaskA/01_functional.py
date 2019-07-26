#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
		'name'        : 'Sat example 1',
		'description' : 'Tests if a sat example works.',
		'arguments'   : 'input.cnf',
		'stdout': """^(Processing Bucket (1|2|3))|({.*})|(SAT)$""",
		'files': {"input.cnf": open('copy/input/sat1.cnf', 'r').read()},
	},
	{
		'name'        : 'Sat example 2',
		'description' : 'Tests if a sat example works.',
		'arguments'   : 'input.cnf',
		'stdout': """^(Processing Bucket (1|2|3|4))|({.*})|(SAT)$""",
		'files': {"input.cnf": open('copy/input/sat2.cnf', 'r').read()},
	},
	{
		'name'        : 'Sat example 3',
		'description' : 'Tests if a sat example works.',
		'arguments'   : 'input.cnf',
		'stdout': """^(Processing Bucket ([1-6]))|({.*})|(SAT)$""",
		'files': {"input.cnf": open('copy/input/sat3.cnf', 'r').read()},
	},
	{
		'name'        : 'Sat example 4',
		'description' : 'Tests if a sat example works.',
		'arguments'   : 'input.cnf',
		'stdout': """^(Processing Bucket ([1-9]))|({.*})|(SAT)$""",
		'files': {"input.cnf": open('copy/input/sat4.cnf', 'r').read()},
	},
	{
		'name'        : 'quinn',
		'description' : 'Tests if the quinn example works.',
		'arguments'   : 'input.cnf',
		'stdout': """^(Processing Bucket [1-9]|(1[0-6]))|({.*})|(SAT)$""",
		'files': {"input.cnf": open('copy/input/quinn.cnf', 'r').read()}
	},
	{
		'name'        : 'Unsat example 1',
		'description' : 'Tests if an unsat example works.',
		'arguments'   : 'input.cnf',
		'stdout': """^(Processing Bucket (1|2|3))|({.*})|(UNSAT)$""",
		'files': {"input.cnf": open('copy/input/unsat1.cnf', 'r').read()},
	},
	{
		'name'        : 'Unsat example 2',
		'description' : 'Tests if an unsat example works.',
		'arguments'   : 'input.cnf',
		'stdout': """^(Processing Bucket (1|2|3|4))|({.*})|(UNSAT)$""",
		'files': {"input.cnf": open('copy/input/unsat2.cnf', 'r').read()},
	},
	{
		'name'        : 'Unsat example 3',
		'description' : 'Tests if an unsat example works.',
		'arguments'   : 'input.cnf',
		'stdout': """^(Processing Bucket (1|2|3|4))|({.*})|(UNSAT)$""",
		'files': {"input.cnf": open('copy/input/unsat3.cnf', 'r').read()},
	},
	{
		'name'        : 'Unsat example 4',
		'description' : 'Tests if an unsat example works.',
		'arguments'   : 'input.cnf',
		'stdout': """^(Processing Bucket ([1-5]))|({.*})|(UNSAT)$""",
		'files': {"input.cnf": open('copy/input/unsat4.cnf', 'r').read()},
	},
]

for test in tests:
	test['analysers'] = {
		'stdout': analysers.ExceptionAnalyser(analysers.MatchLineAnalyser(test['stdout'])),
		'stderr': analysers.ExceptionAnalyser()
	}

success = ipo.run(sys.argv[1:], tests,
						  description="These roughly test the functional correctness.")
sys.exit(0 if success else 1)
