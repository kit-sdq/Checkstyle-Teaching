#!/usr/bin/env python
# -*- encoding: utf-8 -*-
import logging
import os

if not(os.environ.get('PYTHOMAT_TESTS_LOG')):
	log_file = os.path.join(os.path.dirname(os.path.abspath(__file__)), "tests.log")
	os.environ['PYTHOMAT_TESTS_LOG'] = log_file
import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'addAssembly',
        'description': 'Tests if the given addAssembly example works.',
        'protocol': 'addAssembly.txt',
    },
	{
        'name': 'removeAssembly',
        'description': 'Tests if the given removeAssembly example works.',
        'protocol': 'removeAssembly.txt',
    },
	{
        'name': 'printAssembly',
        'description': 'Tests if the given printAssembly example works.',
        'protocol': 'printAssembly.txt',
    },
	{
        'name': 'getAssemblies',
        'description': 'Tests if the given getAssemblies example works.',
        'protocol': 'getAssemblies.txt',
    },
	{
        'name': 'getComponents',
        'description': 'Tests if the given getComponents example works.',
        'protocol': 'getComponents.txt',
    },
	{
        'name': 'addPart',
        'description': 'Tests if the given addPart example works.',
        'protocol': 'addPart.txt',
    },
	{
        'name': 'removePart',
        'description': 'Tests if the given removePart example works.',
        'protocol': 'removePart.txt',
    },
	{
        'name': 'public with changed names',
        'description': 'Tests if the given example interaction works with different names.',
        'protocol': 'publicchangednames.txt',
    }
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/functional_basic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if the data system in general works correctly with correct input.")
sys.exit(0 if success else 1)
