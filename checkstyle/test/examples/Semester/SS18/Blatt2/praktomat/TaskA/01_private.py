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
        "stdout": """8,4,2,1,6,3,16,8,4,2,1,6,3,16,8,4,2,1,6,3
                    54.5 59
                    1 * 11 = 11
                    2 * 11 = 22
                    3 * 11 = 33
                    4 * 11 = 44
                    5 * 11 = 55
                    6 * 11 = 66
                    7 * 11 = 77
                    8 * 11 = 88
                    9 * 11 = 99
                    10 * 11 = 110
                    1 * 12 = 12
                    2 * 12 = 24
                    3 * 12 = 36
                    4 * 12 = 48
                    5 * 12 = 60
                    6 * 12 = 72
                    7 * 12 = 84
                    8 * 12 = 96
                    9 * 12 = 108
                    10 * 12 = 120
                    1 * 13 = 13
                    2 * 13 = 26
                    3 * 13 = 39
                    4 * 13 = 52
                    5 * 13 = 65
                    6 * 13 = 78
                    7 * 13 = 91
                    8 * 13 = 104
                    9 * 13 = 117
                    10 * 13 = 130
                    1 * 14 = 14
                    2 * 14 = 28
                    3 * 14 = 42
                    4 * 14 = 56
                    5 * 14 = 70
                    6 * 14 = 84
                    7 * 14 = 98
                    8 * 14 = 112
                    9 * 14 = 126
                    10 * 14 = 140
                    1 * 15 = 15
                    2 * 15 = 30
                    3 * 15 = 45
                    4 * 15 = 60
                    5 * 15 = 75
                    6 * 15 = 90
                    7 * 15 = 105
                    8 * 15 = 120
                    9 * 15 = 135
                    10 * 15 = 150
                    1 * 16 = 16
                    2 * 16 = 32
                    3 * 16 = 48
                    4 * 16 = 64
                    5 * 16 = 80
                    6 * 16 = 96
                    7 * 16 = 112
                    8 * 16 = 128
                    9 * 16 = 144
                    10 * 16 = 160
                    1 * 17 = 17
                    2 * 17 = 34
                    3 * 17 = 51
                    4 * 17 = 68
                    5 * 17 = 85
                    6 * 17 = 102
                    7 * 17 = 119
                    8 * 17 = 136
                    9 * 17 = 153
                    10 * 17 = 170
                    1 * 18 = 18
                    2 * 18 = 36
                    3 * 18 = 54
                    4 * 18 = 72
                    5 * 18 = 90
                    6 * 18 = 108
                    7 * 18 = 126
                    8 * 18 = 144
                    9 * 18 = 162
                    10 * 18 = 180
                    1 * 19 = 19
                    2 * 19 = 38
                    3 * 19 = 57
                    4 * 19 = 76
                    5 * 19 = 95
                    6 * 19 = 114
                    7 * 19 = 133
                    8 * 19 = 152
                    9 * 19 = 171
                    10 * 19 = 190
                    1 * 20 = 20
                    2 * 20 = 40
                    3 * 20 = 60
                    4 * 20 = 80
                    5 * 20 = 100
                    6 * 20 = 120
                    7 * 20 = 140
                    8 * 20 = 160
                    9 * 20 = 180
                    10 * 20 = 200"""
    }
]

for test in tests:
    test['analysers'] = {
        'stdout': analysers.ExceptionAnalyser(analysers.LineByLineAnalyser(test["stdout"])),
        'stderr': analysers.ExceptionAnalyser()
    }

success = ipo.run(sys.argv[1:], tests,
                  description="These is a private test for easy correction.")
sys.exit(0 if success else 1)
