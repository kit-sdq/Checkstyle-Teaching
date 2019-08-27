#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': "sieve example",
        'description': "This tests if the prime sieve example works correctly.",
        'arguments': "sieve 20",
        'stdout': """2 3 5 7 11 13 17 19"""
    },
    {
        'name': "sieve end on prime",
        'description': "This tests if the prime sieve works correctly when the end it a prime.",
        'arguments': "sieve 29",
        'stdout': """2 3 5 7 11 13 17 19 23 29"""
    },
    {
        'name': "sieve high end",
        'description': "This tests if the prime sieve works correctly for a high input.",
        'arguments': "egcd 84 20",
        'stdout': """4 1 -4"""
    },
    {
        'name': "egcd one prime",
        'description': "This tests if the egcd example works correctly when given one prime.",
        'arguments': "egcd 36 5",
        'stdout': """1 1 -7"""
    },
    {
        'name': "egcd two primes",
        'description': "This tests if the egcd example works correctly when given two primes.",
        'arguments': "egcd 11 127",
        'stdout': """1 -23 2"""
    },
    {
        'name': "egcd big input lower than MAXINT",
        'description': "This tests if the egcd example works correctly when given two primes.",
        'arguments': "egcd 2050123456 895124876",
        'stdout': """4 -19272914 44141163"""
    },
    {
        'name': "egcd big input bigger than MAXINT",
        'description': "This tests if the egcd example works correctly when given a big input where both numbers are too big for an int.",
        'arguments': "egcd 12131072439211271897323671531612440428472427633701410925634549312301964 37304208561932419736532241686654101705736136521417171171379797429933480",
        'stdout': """4 2173788860897930769728696173795125163600119377306801947233664088679611 -706901209157761673243600515126829336444308436345462582766841098832200"""
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

success = ipo.run(sys.argv[1:], tests, description="This tests if the account methods work correctly.")
sys.exit(0 if success else 1)
