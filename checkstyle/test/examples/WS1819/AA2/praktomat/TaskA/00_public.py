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
        'name': 'Public Test',
        'description': 'Tests an example interaction with basic cases.',
        'protocol': 'public.txt'
    }
]

for test in tests:
    test['protocol'] = 'copy/protocols/public/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These are public tests. Remember, we have got a lot more than just these.")
sys.exit(0 if success else 1)
