#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
        'name': "Correct eulers number approximation",
        'description': "Checks if the approximation is correct for the given n.",
		'stdout': """28.2743
                    18.8495"""
    }
]

for test in tests:
    test['analysers'] = {
        'stdout' : analysers.ExceptionAnalyser(analysers.FloatingPointAnalyser(test['stdout'], 4)),
        'stderr' : analysers.ExceptionAnalyser()
    }

success = ipo.run(sys.argv[1:], tests, description="These are public tests. Remember, we have got a lot more than just these.")
sys.exit(0 if success else 1)