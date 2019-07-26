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
        'name': 'addAssembly wrong Arguments',
        'description': 'Tests if the tested command with wrong arguments is handled correctly.',
        'protocol': 'addAssemblyWrongArgument.txt',
    },
	{
        'name': 'removeAssembly wrong Arguments',
        'description': 'Tests if the tested command with wrong arguments is handled correctly.',
        'protocol': 'removeAssemblyWrongArgument.txt',
    },
	{
        'name': 'printAssembly wrong Arguments',
        'description': 'Tests if the tested command with wrong arguments is handled correctly.',
        'protocol': 'printAssemblyWrongArgument.txt',
    },
	{
        'name': 'getAssemblies wrong Arguments',
        'description': 'Tests if the tested command with wrong arguments is handled correctly.',
        'protocol': 'getAssembliesWrongArgument.txt',
    },
	{
        'name': 'getComponents wrong Arguments',
        'description': 'Tests if the tested command with wrong arguments is handled correctly.',
        'protocol': 'getComponentsWrongArgument.txt',
    },
	{
        'name': 'addPart wrong Arguments',
        'description': 'Tests if the tested command with wrong arguments is handled correctly.',
        'protocol': 'addPartWrongArgument.txt',
    },
	{
        'name': 'removePart wrong Arguments',
        'description': 'Tests if the tested command with wrong arguments is handled correctly.',
        'protocol': 'removePartWrongArgument.txt',
    },
	{
        'name': 'addAssembly with wrong Amounts',
        'description': 'Tests if the tested command with wrong arguments is handled correctly.',
        'protocol': 'addAssemblyInvalidAmount.txt',
    },
	{
        'name': 'addPart with wrong Amounts',
        'description': 'Tests if the tested command with wrong arguments is handled correctly.',
        'protocol': 'addPartInvalidAmount.txt',
    },
	{
        'name': 'removePart with wrong Amounts',
        'description': 'Tests if the tested command with wrong arguments is handled correctly.',
        'protocol': 'removePartInvalidAmount.txt',
    }
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/errsyntactic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="Tests if syntactical errors are correctly handled.")
sys.exit(0 if success else 1)
