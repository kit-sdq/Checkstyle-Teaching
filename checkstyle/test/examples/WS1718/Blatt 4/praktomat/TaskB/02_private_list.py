#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': "add",
        'description': "This tests if adding accounts works correctly.",
        'arguments': "add",
        'stdout': """Added account with state (5,10,0)
        Added account with state (6,12,0)
        Added account with state (7,14,0)"""
    },
    {
        'name': "size",
        'description': "This tests if the size of the list is tracked correctly.",
        'arguments': "size",
        'stdout': """Size: 0
        Added account with state (5,10,0)
        Size: 1
        Added account with state (6,12,0)
        Size: 2
        Added account with state (7,14,0)
        Size: 3"""
    },
    {
        'name': "get",
        'description': "This tests if getting an account from the list works correctly.",
        'arguments': "getML",
        'stdout': """No account at index 0
        Added account with state (5,10,0)
        Account state at index 0: (5,10,0)
        No account at index 1
        Added account with state (6,12,0)
        Added account with state (7,14,0)
        Account state at index 0: (5,10,0)
        Account state at index 1: (6,12,0)
        Account state at index 2: (7,14,0)
        No account at index 3"""
    },
    {
        'name': "getFirst",
        'description': "This tests if getting the first account of the list works correctly.",
        'arguments': "getFirst",
        'stdout': """No account at head of list
        Added account with state (5,10,0)
        Account state at head of list: (5,10,0)
        Added account with state (6,12,0)
        Added account with state (7,14,0)
        Account state at head of list: (5,10,0)"""
    },
    {
        'name': "getLast",
        'description': "This tests if getting the last account of the list works correctly.",
        'arguments': "getLast",
        'stdout': """No account at tail of list
        Added account with state (5,10,0)
        Account state at tail of list: (5,10,0)
        Added account with state (6,12,0)
        Added account with state (7,14,0)
        Account state at tail of list: (7,14,0)"""
    },
    {
        'name': "add at index",
        'description': "This tests if adding an account at a specific index works correctly.",
        'arguments': "addindex",
        'stdout': """Added account with state (5,10,0)
        Added account with state (6,12,0)
        Added account with state (7,14,0)
        Added account with state (8,16,0)
        Account state at index 0: (5,10,0)
        Account state at index 1: (8,16,0)
        Account state at index 2: (6,12,0)
        Account state at index 3: (7,14,0)
        Added account with state (9,18,0)
        Account state at index 0: (5,10,0)
        Account state at index 1: (8,16,0)
        Account state at index 2: (6,12,0)
        Account state at index 3: (9,18,0)
        Account state at index 4: (7,14,0)"""
    },
    {
        'name': "add at first index",
        'description': "This tests if adding an account at the head of the list works correctly.",
        'arguments': "addindexfirst",
        'stdout': """Added account with state (5,10,0)
        Added account with state (6,12,0)
        Account state at index 0: (6,12,0)
        Account state at index 1: (5,10,0)
        Added account with state (7,14,0)
        Account state at index 0: (7,14,0)
        Account state at index 1: (6,12,0)
        Account state at index 2: (5,10,0)"""
    },
    {
        'name': "add at last index",
        'description': "This tests if adding an account at the tail of the list works correctly.",
        'arguments': "addindexlast",
        'stdout': """Added account with state (5,10,0)
        Added account with state (6,12,0)
        Account state at index 0: (5,10,0)
        Account state at index 1: (6,12,0)
        Added account with state (7,14,0)
        Account state at index 0: (5,10,0)
        Account state at index 1: (6,12,0)
        Account state at index 2: (7,14,0)"""
    },
    {
        'name': "add at invalid index",
        'description': "This tests if adding an account at an invalid index is handled correctly.",
        'arguments': "addindexinvalid",
        'stdout': """Added account with state (5,10,0)
        Added account with state (6,12,0)
        Account state at index 0: (5,10,0)
        Account state at index 1: (6,12,0)
        Added account with state (7,14,0)
        Account state at index 0: (5,10,0)
        Account state at index 1: (6,12,0)
        Account state at index 2: (7,14,0)"""
    },
    {
        'name': "remove",
        'description': "This tests if removing an account at a specific index from the list works correctly.",
        'arguments': "removeML",
        'stdout': """Added account with state (5,10,0)
        Added account with state (6,12,0)
        Added account with state (7,14,0)
        Added account with state (8,16,0)
        Removal of account at index 2 successful
        Account state at index 0: (5,10,0)
        Account state at index 1: (6,12,0)
        Account state at index 2: (8,16,0)
        Removal of account at index 1 successful
        Account state at index 0: (5,10,0)
        Account state at index 1: (8,16,0)"""
    },
    {
        'name': "remove first",
        'description': "This tests if removing the head of the list works correctly.",
        'arguments': "removefirst",
        'stdout': """Added account with state (5,10,0)
        Added account with state (6,12,0)
        Added account with state (7,14,0)
        Removal of account at index 0 successful
        Account state at index 0: (6,12,0)
        Account state at index 1: (7,14,0)"""
    },
    {
        'name': "remove end",
        'description': "This tests if removing the tail of the list works correctly.",
        'arguments': "removeend",
        'stdout': """Added account with state (5,10,0)
        Added account with state (6,12,0)
        Added account with state (7,14,0)
        Removal of account at index 2 successful
        Account state at index 0: (5,10,0)
        Account state at index 1: (6,12,0)"""
    },
    {
        'name': "remove last",
        'description': "This tests if the last account in the list works correctly.",
        'arguments': "removelast",
        'stdout': """Added account with state (5,10,0)
        Removal of account at index 0 successful
        List is empty"""
    },
    {
        'name': "remove nonexisting account",
        'description': "This tests if removing from an invalid index is handled correctly.",
        'arguments': "removeinvalid",
        'stdout': """Removal of account at index 0 failed
        Added account with state (5,10,0)
        Added account with state (6,12,0)
        Added account with state (7,14,0)
        Removal of account at index -1 failed
        Removal of account at index 9 failed
        Account state at index 0: (5,10,0)
        Account state at index 1: (6,12,0)
        Account state at index 2: (7,14,0)"""
    },
    {
        'name': "contains",
        'description': "This tests if the contains check works correctly.",
        'arguments': "containsML",
        'stdout': """Added account with state (5,10,0)
        Added account with state (6,12,0)
        Added account with state (7,14,0)
        Account with state (5,10,0) exists
        Account with state (6,12,0) exists
        Account with state (7,14,0) exists
        Account with state (8,14,0) doesn't exist
        Account with state (7,16,0) doesn't exist
        Account with state (8,16,0) doesn't exist"""
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

success = ipo.run(sys.argv[1:], tests, description="This tests if the MinimalList methods work correctly.")
sys.exit(0 if success else 1)
