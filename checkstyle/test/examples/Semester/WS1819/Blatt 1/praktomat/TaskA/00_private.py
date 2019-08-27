#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': "Correct output",
        'description': "Checks if the output is correct.",
        'stdout': """Die Anmeldefrist fuer die Praesenzuebung endet am 10.01.2019 um 12:00 Uhr.
                     Die Anmeldung fuer die Abschlussaufgaben ist zwischen 10.02.2019-20.02.2019 moeglich."""
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
