#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'Simple Booking Interaction',
        'description': 'Tests if the add followed by a book command works correctly.',
        'protocol': 'public.txt',
    }
]

for test in tests:
    test['protocol'] = 'copy/protocols/public/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These are public tests. Remember, we have got a lot more than just these.")
sys.exit(0 if success else 1)
