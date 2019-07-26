#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': "min max after modification",
        'description': "Checks if the minimum and maximum methods work correctly after modification of tupels through insert and remove.",
        'arguments': "INTERACTION MODIFYMINMAX",
        'stdout': open('copy/expected/interaction/modifyminmax.txt', 'r').read()
    },
    {
        'name': "min max after swapping",
        'description': "Checks if the minimum and maximum methods work correctly after swapping values around. The return values shouldn't be affected.",
        'arguments': "INTERACTION SWAPMINMAX",
        'stdout': open('copy/expected/interaction/swapminmax.txt', 'r').read()
    },
    {
        'name': "indexOf after swapping",
        'description': "Checks if the indexOf method works correctly after swapping values around.",
        'arguments': "INTERACTION INDEXOFSWAP",
        'stdout': open('copy/expected/interaction/indexofswap.txt', 'r').read()
    },
    {
        'name': "equals after modification and swapping",
        'description': "Checks if the equals method works correctly after modifcation of tuples through insert, remove and swap.",
        'arguments': "INTERACTION EQUALS",
        'stdout': open('copy/expected/interaction/equals.txt', 'r').read()
    },
    {
        'name': "Immutability of tuples",
        'description': "Checks if the tuple on which a changing method is called stays the same.",
        'arguments': "INTERACTION CHANGE",
        'stdout': open('copy/expected/interaction/change.txt', 'r').read()
    }
]

for test in tests:
    test['analysers'] = {
        'stdout': analysers.ExceptionAnalyser(analysers.LineByLineAnalyser(test['stdout'])),
        'stderr': analysers.ExceptionAnalyser()
    }

success = ipo.run(sys.argv[1:], tests,
                  description="This private test tests the interaction between different methods. A good rating requires all these tests to be passed.",
                  main="Wrapper")
sys.exit(0 if success else 1)
