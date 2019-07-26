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
        'stdout': """Werfen Sie den Disclaimer bis zum 19.04.2018 in den Programmieren-Briefkasten im Gebäude 50.34 ein.
                    Die Anmeldefrist für die Präsenzübung endet am 15.06.2018 um 12 Uhr.
                    Die Anmeldung für die Abschlussaufgaben ist zwischen 09.07.2018 und 16.07.2018 bis 12 Uhr möglich."""
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
