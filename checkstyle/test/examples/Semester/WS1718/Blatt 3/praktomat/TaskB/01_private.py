#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': "Win horizontal top row",
        'description': "This tests if winning in the first row works correctly.",
        'arguments': "0 6 1 3 2 4 7 8 5",
        'stdout': """P1 wins 5"""
    },
    {
        'name': "Win horizontal middle row",
        'description': "This tests if winning in the middle row works correctly.",
        'arguments': "5 8 4 6 3 1 7 2 0",
        'stdout': """P1 wins 5"""
    },
    {
        'name': "Win horizontal bottom row",
        'description': "This tests if winning in the bottom row works correctly.",
        'arguments': "6 0 8 2 7 4 3 5 1",
        'stdout': """P1 wins 5"""
    },
    {
        'name': "Win vertical left column",
        'description': "This tests if winning in the left column works correctly.",
        'arguments': "0 1 3 2 6 4 5 8 7",
        'stdout': """P1 wins 5"""
    },
    {
        'name': "Win vertical middle column",
        'description': "This tests if winning in the middle column works correctly.",
        'arguments': "7 8 4 3 1 0 6 2 5",
        'stdout': """P1 wins 5"""
    },
    {
        'name': "Win vertical right column",
        'description': "This tests if winning in the right column works correctly.",
        'arguments': "2 1 8 4 5 0 7 6 3",
        'stdout': """P1 wins 5"""
    },
    {
        'name': "Win diagnoal",
        'description': "This tests if on the diagonal works correctly.",
        'arguments': "0 1 4 5 8 3 7 6 2",
        'stdout': """P1 wins 5"""
    },
    {
        'name': "Win antidiagonal",
        'description': "This tests if winning on the antidiagonal works correctly.",
        'arguments': "2 1 4 0 6 3 5 8 7",
        'stdout': """P1 wins 5"""
    },
    {
        'name': "Win last turn",
        'description': "This tests if winning on last turn works correctly.",
        'arguments': "0 1 3 6 7 8 4 2 5",
        'stdout': """P1 wins 9"""
    },
    {
        'name': "P2 wins",
        'description': "This tests if P2 can win.",
        'arguments': "3 0 1 4 7 8 2 6 5",
        'stdout': """P2 wins 6"""
    },
    {
        'name': "Draw",
        'description': "This tests if drawing a game works correctly.",
        'arguments': "3 0 1 4 2 5 7 6 8",
        'stdout': """draw"""
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
