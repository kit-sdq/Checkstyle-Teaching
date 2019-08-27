#!/usr/bin/env python

import sys
sys.path.append('pythomat.zip')

from pythomat import interactive
from pythomat import analysers
from pythomat.analysers import *

tests = [
         {
         'name'        : 'Roll - moving out of base',
         'description' : 'Tests if moving out of the base works as expected.',
         'protocol'    : '01_roll_00_movingout.txt',
         },
         {
         'name'        : 'Roll - rolling again after six',
         'description' : 'Tests if rolling a six leads to the expected behavior.',
         'protocol'    : '01_roll_01_rollingaftersix.txt',
         },
         {
         'name'        : 'Roll - not moving over own start',
         'description' : 'Tests if the roll command does not offer moves over the start field of the current player.',
         'protocol'    : '01_roll_02_notmovingoverownstart.txt',
         },
         {
         'name'        : 'Roll - sorted output',
         'description' : 'Tests if the output is sorted as described in the task.',
         'protocol'    : '01_roll_03_sortedoutput.txt',
         },
         {
         'name'        : 'Roll - no move possible with 6 as rolled value',
         'description' : 'Tests if the roll command maintains the player as described in the task.',
         'protocol'    : '01_roll_04_rollsixnomove.txt',
         },
         {
         'name'        : 'Roll - clear start field',
         'description' : 'Tests if the roll command prioritizes clearing the start field as described in the task.',
         'protocol'    : '01_roll_05_baseblocked.txt',
         },
         {
         'name'        : 'Move - win',
         'description' : 'Tests if the output of the move command is expected in case that the active player won.',
         'protocol'    : '02_move_00_win.txt',
         },
         {
         'name'        : 'Print - all base',
         'description' : 'Tests if the print command prints the expected output in case that all tokens are on the base-fields.',
         'protocol'    : '03_print_00_allbase.txt',
         },
		 {
         'name'        : 'Move - rolling after start move',
         'description' : 'Tests if moving out of a base field changes the player.',
         'protocol'    : '01_roll_01_rollingaftersixstart.txt',
         },
		 {
         'name'        : 'Roll - rolling and moving with a six for a few times',
         'description' : 'Tests if rolling a six and moving does not change the player.',
         'protocol'    : '01_roll_01_sixoften.txt',
         },
		 {
         'name'        : 'Roll - rolling six next roll',
         'description' : 'Tests if rolling a six while being not able to move does not change the player.',
         'protocol'    : '01_roll_01_sixobstructed.txt',
         },
		 {
         'name'        : 'Print - sorted random starts',
         'description' : 'Tests if print works.',
         'protocol'    : '01_roll_03_sortedprint.txt',
         },
		 {
         'name'        : 'Print - after some moves',
         'description' : 'Tests if print works.',
         'protocol'    : '02_move_00_printaftermoves.txt',
         },
		 {
         'name'        : 'Move - taking other players pawns works',
         'description' : 'Tests if taking pawns works.',
         'protocol'    : '02_move_00_take.txt',
         },
         ]

for test in tests:
    test.setdefault('protocol_code', 'copy/protocols/code.txt')
    test.setdefault('protocol_var', 'copy/protocols/variables.txt')
    test['protocol'] = 'copy/protocols/01_private/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests, excludemain = '', description='These are private tests. We test extended functionality here.')
sys.exit(0 if success else 1)
