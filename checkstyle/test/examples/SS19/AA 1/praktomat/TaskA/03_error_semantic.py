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
        'name': 'unknown game mode',
        'description': 'Tests if an unknown game mode is handled correctly.',
        'protocol': 'error.txt',
        'arguments': '3d 32',
    },
    {
        'name': 'token count too high',
        'description': 'Tests if a too high token count is handled correctly.',
        'protocol': 'error.txt',
        'arguments': 'standard 33',
    }
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/error_semantic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if logical and semantic errors are handled correctly.")
sys.exit(0 if success else 1)
