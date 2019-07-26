#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'Simple game',
        'description': 'Tests if the command line parsing and the commands are working.',
        'protocol': 'public.txt',
		'arguments': '5;red;1;1;blue;4;4;yellow;2;3;green;2;2',
    }
]

for test in tests:
    test['protocol'] = 'copy/protocols/public/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These are public tests. Remember, we have got a lot more than just these.")
sys.exit(0 if success else 1)
