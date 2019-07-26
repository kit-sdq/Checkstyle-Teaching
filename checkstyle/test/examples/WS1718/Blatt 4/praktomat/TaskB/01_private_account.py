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
        'stdout': """Created account with state (5,10,0)
        Deposited 25, new account state: (5,10,25)
        Deposited 13, new account state: (5,10,38)"""
    },
    {
        'name': "withdraw",
        'description': "This tests if withdrawing money works correctly.",
        'arguments': "withdraw",
        'stdout': """Created account with state (5,10,0)
        Deposited 25, new account state: (5,10,25)
        Withdrawal of 11 successful, new account state: (5,10,14)
        Withdrawal of 14 successful, new account state: (5,10,0)"""
    },
    {
        'name': "withdraw too much",
        'description': "This tests if withdrawing too much money is correctly handled.",
        'arguments': "withdrawtoomuch",
        'stdout': """Created account with state (5,10,0)
        Withdrawal of 5 failed, new account state: (5,10,0)
        Deposited 2, new account state: (5,10,2)
        Withdrawal of 10 failed, new account state: (5,10,2)
        Withdrawal of 3 failed, new account state: (5,10,2)"""
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
