#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': "deposit",
        'description': "This tests if depositing money works correctly.",
        'arguments': "deposit",
        'stdout': """Deposited 25, new balance: 25
        Deposited 13, new balance: 38"""
    },
    {
        'name': "withdraw",
        'description': "This tests if withdrawing money works correctly.",
        'arguments': "withdraw",
        'stdout': """Deposited 25, new balance: 25
        Withdrawal of 11 successful, new balance: 14
        Withdrawal of 14 successful, new balance: 0"""
    },
    {
        'name': "withdraw too much",
        'description': "This tests if withdrawing too much money is correctly handled.",
        'arguments': "withdrawtoomuch",
        'stdout': """Withdrawel of 5 failed
        Deposited 2, new balance: 2
        Withdrawel of 10 failed
        Withdrawel of 3 failed"""
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
