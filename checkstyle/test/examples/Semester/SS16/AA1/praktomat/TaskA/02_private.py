#!/usr/bin/env python

import sys
sys.path.append('pythomat.zip')

from pythomat import interactive
from pythomat import analysers
from pythomat.analysers import *

tests = [
         {
         'name'        : 'Start - any order',
         'description' : 'Tests if the order of the optional rules has any influence.',
         'protocol'    : '00_start_00_anyorder.txt',
         },
         {
         'name'        : 'Start - duplicates',
         'description' : 'Tests if duplicates are ignored as expected.',
         'protocol'    : '00_start_01_distinct.txt',
         },
         {
         'name'        : 'Barrier - general',
         'description' : 'Tests if barrier works as expected.',
         'protocol'    : '01_barrier_00_general.txt',
         },
         {
         'name'        : 'Barrier - start blocked',
         'description' : 'Tests if barrier works as expected in case that a barrier blocks moving out of the start field.',
         'protocol'    : '01_barrier_01_startblocked.txt',
         },
         {
         'name'        : 'Backward - general',
         'description' : 'Tests if backward works as expected.',
         'protocol'    : '02_backward_00_general.txt',
         },
         {
         'name'        : 'Backward - on own start',
         'description' : 'Tests if backward works as expected in case that a token of another player is placed on the start-field of the current player.',
         'protocol'    : '02_backward_01_onownstart.txt',
         },
         {
         'name'        : 'Nojump - general',
         'description' : 'Tests if nojump works as expected.',
         'protocol'    : '03_nojump_00_general.txt',
         },
         {
         'name'        : 'Triply - general',
         'description' : 'Tests if triply works as expected.',
         'protocol'    : '04_triply_00_general.txt',
         },
         {
         'name'        : 'Print - general',
         'description' : 'Tests if print works as expected (with optional rules).',
         'protocol'    : '05_print_00_general.txt',
         },
         ]

for test in tests:
    test.setdefault('protocol_code', 'copy/protocols/code.txt')
    test.setdefault('protocol_var', 'copy/protocols/variables.txt')
    test['protocol'] = 'copy/protocols/02_private/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests, excludemain = '', description='These are private tests. We test extended functionality here.')
sys.exit(0 if success else 1)
