#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

import tsp
from pythomat import analysers

tests = [
    {
        "name" : "Beispiel aus der Aufgabenbeschreibung",
        "files" : {
            "public.txt" :
"""1,2,3
1,3,4
1,5,7
1,4,2
2,3,9
2,4,6
2,5,1
3,4,5
3,5,8
4,5,1"""
        },
        "analysers" : { 'stdout' : analysers.LineByLineAnalyser(["1,2,5,4,3,1"]) }
    },{
        "name" : "Fehlerbehandlung 1",
        "files" : {
            "error1" :
"""1,100,100
1,101,101"""
        },
        "analysers" : { 'stdout' : analysers.ErrorAnalyser() }
    },{
        "name" : "Mini-Beispiel",
        "files" : {
            "mini" :
"""1,2,12
1,3,13
3,2,11
1,4,10
4,2,14"""
        },
        "analysers" : { 'stdout' : analysers.LineByLineAnalyser(["1,4,2,3,1"]) }
    }
]

for test in tests:
    test ["arguments"] = next(iter(test["files"]))

success =  tsp.run(sys.argv[1:], tests)
sys.exit(0 if success else 1)
