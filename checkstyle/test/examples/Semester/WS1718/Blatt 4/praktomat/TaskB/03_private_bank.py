#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': "createAccount",
        'description': "This tests if creating accounts works correctly.",
        'arguments': "create",
        'stdout': """Created account at 0"""
    },
    {
        'name': "getAccount",
        'description': "This tests if the getAccount method works correctly.",
        'arguments': "get",
        'stdout': """Created account at 0
        Created account at 1
        Created account at 2
        Account state at 0: (151,1,0)
        Account state at 1: (151,2,0)
        Account state at 2: (151,3,0)"""
    },
    {
        'name': "get empty index",
        'description': "This tests if trying to get an empty index is correctly handled.",
        'arguments': "getempty",
        'stdout': """No account at 0
        Created account at 0
        No account at 1
        No account at 10"""
    },
    {
        'name': "createAccount multiple",
        'description': "This tests if creating multiple accounts works correctly.",
        'arguments': "createmultiple",
        'stdout': """Created account at 0
        Created account at 1
        Created account at 2
        Created account at 3
        Created account at 4
        Created account at 5
        Created account at 6
        Created account at 7
        Created account at 8
        Account state at index 0: (151,1,0)
        Account state at index 1: (151,2,0)
        Account state at index 2: (151,3,0)
        Account state at index 3: (151,4,0)
        Account state at index 4: (151,5,0)
        Account state at index 5: (151,6,0)
        Account state at index 6: (151,7,0)
        Account state at index 7: (151,8,0)
        Account state at index 8: (151,9,0)"""
    },
    {
        'name': "length",
        'description': "This tests if if the length method works correctly.",
        'arguments': "length",
        'stdout': """Number of accounts: 0
        Created account at 0
        Number of accounts: 1"""
    },
    {
        'name': "create unordered",
        'description': "This tests creating account in an unordered manner works correctly.",
        'arguments': "createunordered",
        'stdout': """Created account at 0
        Created account at 1
        Created account at 1
        Created account at 2
        Created account at 1
        Created account at 5
        Created account at 0
        Created account at 7
        Created account at 5
        Account state at index 0: (151,1,0)
        Account state at index 1: (151,2,0)
        Account state at index 2: (151,3,0)
        Account state at index 3: (151,4,0)
        Account state at index 4: (151,5,0)
        Account state at index 5: (151,6,0)
        Account state at index 6: (151,7,0)
        Account state at index 7: (151,8,0)
        Account state at index 8: (151,9,0)"""
    },
    {
        'name': "removeAccount",
        'description': "This tests if the removal of accounts works correctly.",
        'arguments': "remove",
        'stdout': """Created account at 0
        Created account at 1
        Created account at 2
        Account state at index 0: (151,1,0)
        Account state at index 1: (151,2,0)
        Account state at index 2: (151,3,0)
        Removal of account with number 1 successful
        Account state at index 0: (151,2,0)
        Account state at index 1: (151,3,0)
        Removal of account with number 3 successful
        Account state at index 0: (151,2,0)
        Created account at 1
        Created account at 2
        Removal of account with number 35 successful
        Account state at index 0: (151,2,0)
        Account state at index 1: (151,36,0)"""
    },
    {
        'name': "remove nonexisting accounts",
        'description': "This tests if removing nonexisting accounts is correctly handled.",
        'arguments': "removenonexisting",
        'stdout': """Removal of account with number 1 failed
        Created account at 0
        Created account at 1
        Created account at 2
        Removal of account with number 200 failed
        Account state at index 0: (151,1,0)
        Account state at index 1: (151,2,0)
        Account state at index 2: (151,3,0)"""
    },
    {
        'name': "contains",
        'description': "This tests if the contains method works correctly.",
        'arguments': "contains",
        'stdout': """Bank doesn't contain account with number 1
        Created account at 0
        Created account at 1
        Created account at 2
        Bank contains account with number 2
        Bank contains account with number 1
        Bank contains account with number 3
        Bank doesn't contain account with number 25"""
    },
    {
        'name': "transfer",
        'description': "This tests if transferring money works correctly.",
        'arguments': "transfer",
        'stdout': """Created account at 0
        Created account at 1
        Deposited 10, new account state: (151,1,10)
        Transfer of 5 succeeded
        Account state at index 0: (151,1,5)
        Account state at index 1: (151,2,5)
        Transfer of 2 succeeded
        Account state at index 0: (151,1,7)
        Account state at index 1: (151,2,3)"""
    },
    {
        'name': "transfer from missing source",
        'description': "This tests if transferring from a nonexistant source is correctly handled.",
        'arguments': "transfernosource",
        'stdout': """Created account at 0
        Deposited 10, new account state: (151,2,10)
        Transfer of 5 failed
        Account state at index 0: (151,2,10)"""
    },
    {
        'name': "transfer to missing target",
        'description': "This tests if transferring to a nonexistant target is correctly handled.",
        'arguments': "transfernotarget",
        'stdout': """Created account at 0
        Deposited 10, new account state: (151,1,10)
        Transfer of 5 failed
        Account state at index 0: (151,1,10)"""
    },
    {
        'name': "transfer balance too low",
        'description': "This tests if transferring nonexistant money is correctly handled.",
        'arguments': "transfernomoney",
        'stdout': """Created account at 0
        Created account at 1
        Transfer of 5 failed
        Deposited 2, new account state: (151,1,2)
        Transfer of 3 failed
        Account state at index 0: (151,1,2)
        Account state at index 1: (151,2,0)"""
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

success = ipo.run(sys.argv[1:], tests, description="This tests if the bank methods work correctly.")
sys.exit(0 if success else 1)
