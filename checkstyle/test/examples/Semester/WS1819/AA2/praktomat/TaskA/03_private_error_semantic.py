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
        'name': 'addAssemblyCycle',
        'description': 'Tests if the handling of cycles is correct.',
        'protocol': 'addAssemblyCycle.txt',
    },
	{
        'name': 'addAssemblySimpleCycle',
        'description': 'Tests if the handling of cycles is correct.',
        'protocol': 'addAssemblySimpleCycle.txt',
    },
	{
        'name': 'addPartSimpleCycle',
        'description': 'Tests if the handling of cycles is correct.',
        'protocol': 'addPartSimpleCycle.txt',
    },
	{
        'name': 'addPartCycle',
        'description': 'Tests if the handling of cycles is correct.',
        'protocol': 'addPartCycle.txt',
    },
	{
        'name': 'addAssemblyExisting',
        'description': 'Tests if the handling of existing things is correct.',
        'protocol': 'addAssemblyExisting.txt',
    },
	{
        'name': 'addPartNotExisting',
        'description': 'Tests if the handling of not existing things is correct.',
        'protocol': 'addPartNotExisting.txt',
    },
	{
        'name': 'removePartNotExisting',
        'description': 'Tests if the handling of not existing things is correct.',
        'protocol': 'removePartNotExisting.txt',
    },
	{
        'name': 'getAssembliesNotExisting',
        'description': 'Tests if the handling of not existing things is correct.',
        'protocol': 'getAssembliesNotExisting.txt',
    },
	{
        'name': 'getComponentsNotExisting',
        'description': 'Tests if the handling of not existing things is correct.',
        'protocol': 'getComponentsNotExisting.txt',
    },
	{
        'name': 'leadingZeros',
        'description': 'Tests if the handling of leading zeros is correct.',
        'protocol': 'leadingZeros.txt',
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/errsemantic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="Tests if semantical errors are correctly handled.")
sys.exit(0 if success else 1)
