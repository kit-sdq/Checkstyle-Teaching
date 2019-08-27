#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': "Correct calculation diameter 3 height 4",
        'description': "Checks if the result ist correct for a diameter of 3 and a height of 4.",
        "arguments": "3 4",
        'stdout': "28\.27433.*;37\.69911.*"
    }
]

for test in tests:
    test['analysers'] = {
        'stdout': analysers.ExceptionAnalyser(analysers.MatchLineAnalyser(test["stdout"], message="Unexpected line, at least one of the results is either wrong or not exact enough. Expected 28.27433xxx;37.69911xxx")),
        'stderr': analysers.ExceptionAnalyser()
    }

success = ipo.run(sys.argv[1:], tests,
                  description="These is a private test for easy correction.")
sys.exit(0 if success else 1)
