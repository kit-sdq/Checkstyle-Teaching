#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'Tests matrix-addition',
        'description': 'Tests if the matrix-addition command works correctly.',
        'protocol': 'functional_addition.txt',
    },
	{
        'name': 'Tests matrix-multiplication',
        'description': 'Tests if the matrix-multiplication command works correctly.',
        'protocol': 'functional_multiplication.txt',
    },
	{
        'name': 'Tests scalar-multiplication',
        'description': 'Tests if the scalar-multiplication command works correctly.',
        'protocol': 'functional_scalar.txt',
    },
	{
        'name': 'Tests transposition',
        'description': 'Tests if the transposition command works correctly.',
        'protocol': 'functional_transposition.txt',
    },
	{
        'name': 'Tests main-diagonal',
        'description': 'Tests if the main-diagonal command works correctly.',
        'protocol': 'functional_diagonal.txt',
    }
    
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/functional/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if the program works correctly with correct input.")
sys.exit(0 if success else 1)
