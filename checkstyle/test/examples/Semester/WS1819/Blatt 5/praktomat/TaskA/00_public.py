#!/usr/bin/env python

import sys
sys.path.append('pythomat.zip')

from pythomat import interactive
from pythomat import analysers
from pythomat.analysers import *

tests = [
         {
         'name'        : 'Public Test',
         'description' : 'Tests if the exemplary interaction on the task sheet works as expected.',
         'protocol'    : '00_tasksheetexample.txt',
         },
         ]

for test in tests:
    test.setdefault('protocol_code', 'copy/protocols/code.txt')
    test.setdefault('protocol_var', 'copy/protocols/variables.txt')
    test['protocol'] = 'copy/protocols/00_public/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests, excludemain = '', description='These are public tests. Remember, we have got a lot more than just these.')
sys.exit(0 if success else 1)
