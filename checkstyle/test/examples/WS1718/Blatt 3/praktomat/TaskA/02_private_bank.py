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
        'stdout': """Created account at 0
        Created account at 1
        Created account at 2"""
    },
    {
        'name': "length",
        'description': "This tests if if the length method works correctly.",
        'arguments': "length",
        'stdout': """Number of account slots: 8
        Created account at 0
        Number of account slots: 8"""
    },
    {
        'name': "size",
        'description': "This tests if the size method works correctly.",
        'arguments': "size",
        'stdout': """Number of accounts: 0
        Created account at 0
        Number of accounts: 1
        Created account at 1
        Created account at 2
        Number of accounts: 3"""
    },
    {
        'name': "create and increase size",
        'description': "This tests if increasing the array when creating too many accounts works correctly.",
        'arguments': "createinc",
        'stdout': """Number of account slots: 8
        Number of accounts: 0
        Created account at 0
        Number of account slots: 8
        Number of accounts: 1
        Created account at 1
        Created account at 2
        Created account at 3
        Created account at 4
        Created account at 5
        Created account at 6
        Created account at 7
        Number of account slots: 8
        Number of accounts: 8
        Created account at 8
        Number of account slots: 16
        Number of accounts: 9"""
    },
    {
        'name': "getAccount",
        'description': "This tests if the getAccount method works correctly.",
        'arguments': "get",
        'stdout': """Created account at 0
        Created account at 1
        Created account at 2
        Account at 0 has the number 1 and a balance of 0
        Account at 1 has the number 170 and a balance of 0
        Account at 2 has the number 5 and a balance of 0"""
    },
    {
        'name': "get empty index",
        'description': "This tests if trying to get an empty index is correctly handled.",
        'arguments': "getempty",
        'stdout': """No account at 0
        Created account at 0
        Created account at 1
        Created account at 2
        Account at 2 has the number 5 and a balance of 0
        No account at 3"""
    },
    {
        'name': "removeAccount",
        'description': "This tests if the removal of accounts works correctly.",
        'arguments': "remove",
        'stdout': """Created account at 0
        Created account at 1
        Created account at 2
        Account at 0 has the number 1 and a balance of 0
        Account at 1 has the number 170 and a balance of 0
        Account at 2 has the number 5 and a balance of 0
        Removal of account 1 successful
        Account at 0 has the number 170 and a balance of 0
        Account at 1 has the number 5 and a balance of 0
        No account at 2
        Removal of account 5 successful
        Account at 0 has the number 170 and a balance of 0
        No account at 1
        Created account at 1
        Created account at 2
        Removal of account 35 successful
        Account at 0 has the number 170 and a balance of 0
        Account at 1 has the number 36 and a balance of 0
        No account at 2"""
    },
    {
        'name': "remove nonexisting accounts",
        'description': "This tests if removing nonexisting accounts is correctly handled.",
        'arguments': "removenonexisting",
        'stdout': """Removal of account 1 failed
        Created account at 0
        Created account at 1
        Created account at 2
        Removal of account 200 failed"""
    },
    {
        'name': "remove and decrease size",
        'description': "This tests if decreasing the array size when too many slots are empty works correctly.",
        'arguments': "removedec",
        'stdout': """Created account at 0
        Created account at 1
        Created account at 2
        Created account at 3
        Created account at 4
        Created account at 5
        Created account at 6
        Created account at 7
        Created account at 8
        Number of account slots: 16
        Number of accounts: 9
        Removal of account 9 successful
        Removal of account 8 successful
        Removal of account 7 successful
        Removal of account 6 successful
        Removal of account 5 successful
        Removal of account 4 successful
        Removal of account 3 successful
        Number of account slots: 16
        Number of accounts: 2
        Removal of account 2 successful
        Number of account slots: 8
        Number of accounts: 1
        Removal of account 1 successful
        Number of account slots: 8
        Number of accounts: 0"""
    },
    {
        'name': "contains",
        'description': "This tests if the contains method works correctly.",
        'arguments': "contains",
        'stdout': """Bank doesn't contain account with number 1
        Created account at 0
        Created account at 1
        Created account at 2
        Bank contains account with number 170
        Bank contains account with number 1
        Bank contains account with number 5
        Bank doesn't contain account with number 25"""
    },
    {
        'name': "internalBankTransfer",
        'description': "This tests if transferring money works correctly.",
        'arguments': "transfer",
        'stdout': """Created account at 0
        Created account at 1
        Deposited 10, new balance: 10
        Transfer of 5 succeeded
        Account at index 0 has a balance of 5
        Account at index 1 has a balance of 5
        Transfer of 2 succeeded
        Account at index 0 has a balance of 7
        Account at index 1 has a balance of 3"""
    },
    {
        'name': "transfer from missing source",
        'description': "This tests if transferring from a nonexistant source is correctly handled.",
        'arguments': "transfernosource",
        'stdout': """Created account at 0
        Deposited 10, new balance: 10
        Transfer of 5 failed"""
    },
    {
        'name': "transfer to missing target",
        'description': "This tests if transferring to a nonexistant target is correctly handled.",
        'arguments': "transfernotarget",
        'stdout': """Created account at 0
        Deposited 10, new balance: 10
        Transfer of 5 failed"""
    },
    {
        'name': "transfer balance too low",
        'description': "This tests if transferring nonexistant money is correctly handled.",
        'arguments': "transfernomoney",
        'stdout': """Created account at 0
        Created account at 1
        Transfer of 5 failed
        Deposited 2, new balance: 2
        Transfer of 3 failed"""
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
