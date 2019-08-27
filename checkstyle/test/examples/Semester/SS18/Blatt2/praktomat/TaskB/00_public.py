#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': "Correct calculation diameter 2 height 4",
        'description': "Checks if the result ist correct for a diameter of 2 and a height of 4.",
        "arguments": "2 4",
        'stdout': "12\.56637.*;25\.13274.*"
    }
]

for test in tests:
    test['analysers'] = {
        'stdout': analysers.ExceptionAnalyser(analysers.MatchLineAnalyser(test["stdout"], message="Unexpected line, at least one of the results is either wrong or not exact enough. Expected 12.56637xxx;25.13274xxx")),
        'stderr': analysers.ExceptionAnalyser()
    }

success = ipo.run(sys.argv[1:], tests,
                  description="These are public tests. Remember, we have got a lot more than just these.")
sys.exit(0 if success else 1)
