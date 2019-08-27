#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': "Method Check",
        'description': "This tests checks if all methods are at least available to be called. It assumes that some getters are available, so watch out for that!",
        'arguments': "methodcheck",
        'stdout': """Checking methods of MinimalList...
        void add(Account): found
        void add(Account, int): found
        boolean remove(int): found
        Account getFirst(): found
        Account getLast(): found
        Account get(int): found
        boolean contains(Account): found
        int size(): found
        
        Checking methods of Bank...
        int createAccount(int): found
        boolean removeAccount(int): found
        boolean containsAccount(int): found
        boolean transfer(int,int,int): found
        int length(): found
        Account getAccount(int): found
        int getBankCode(): found
         
        Checking methods of Account...
        boolean withdraw(int): found
        void deposit(int): found
        int getBalance(): found
        int getAccountNumber(): found
        int getBankAccount(): found"""
    },
    {
        'name': "Constructor Check",
        'description': "This tests checks if all constructors are at least available to be called.",
        'arguments': "constructorcheck",
        'stdout': """Checking contructors of Bank...
        Bank(int): found
         
        Checking contructors of Account...
        Account(int,int): found
         
        Checking contructors of PrivateAcount...
        PrivateAccount(int,int): found
         
        Checking contructors of BusinessAccount...
        BusinessAccount(int,int): found"""
    },
    {
        'name': "Creation",
        'description': "This tests if creation of all classes works correctly.",
        'arguments': "creation",
        'stdout': """Created bank with bankcode 5.
        Created account with bankcode 5 and account number 10.
        Created private account with bankcode 6 and account number 12.
        Created business account with bankcode 7 and account number 14."""
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

success = ipo.run(sys.argv[1:], tests,
                  description="These are public tests. Remember, we have got a lot more than just these.")
sys.exit(0 if success else 1)
