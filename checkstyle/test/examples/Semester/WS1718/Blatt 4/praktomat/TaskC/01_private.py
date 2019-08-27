#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': "sieve end on prime",
        'description': "This tests if the prime sieve works correctly when the end it a prime.",
        'protocol': 'sieveendprime.txt',
    },
    {
        'name': "sieve high end",
        'description': "This tests if the prime sieve works correctly for a high input.",
        'protocol': 'sievehighend.txt',
    },
    {
        'name': "keypair example",
        'description': "This tests if the keypair command works correctly for the example from the assignment sheet.",
        'protocol': 'keypairexample.txt',
    },
    {
        'name': "keypair high e",
        'description': "This tests if the keypair command works correctly finding a \"high\" e.",
        'protocol': 'keypairhighe.txt',
    },
    {
        'name': "keypair BigInteger input",
        'description': "This tests if the keypair command works correctly with a really big input.",
        'protocol': 'keypairbig.txt',
    },
    {
        'name': "crypt BigInteger input",
        'description': "This tests if the crypt command works correctly with a really big input.",
        'protocol': 'cryptbig.txt',
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="This tests more advanced cases of the commands.")
sys.exit(0 if success else 1)
