#!/usr/bin/env python

import sys
sys.path.append('pythomat.zip')

from pythomat import interactive
from pythomat import analysers
from pythomat.analysers import *

tests = [
         {
         'name'        : 'General test for invalid commands',
         'description' : 'Tests if an error-message is printed in case that unknown commands or known commands with surrounding whitespace are provided.',
         'protocol'    : '00_general.txt',
         },
         {
         'name'        : 'Start - active game',
         'description' : 'Tests if an error-message is printed in case that the start command is executed while a game is already active.',
         'protocol'    : '01_start_00_activegame.txt',
         },
         {
         'name'        : 'Start - incorrect parameters',
         'description' : 'Tests if an error-message is printed in case that the provided arguments are invalid.',
         'protocol'    : '01_start_01_incorrectparameters.txt',
         },
         {
         'name'        : 'Start - whitespace',
         'description' : 'Tests if an error-message is printed in case that the start command contains an incorrect number of whitespace characters.',
         'protocol'    : '01_start_02_whitespace.txt',
         },
         {
         'name'        : 'Start - invalid rule',
         'description' : 'Tests if an error-message is printed in case that an invalid rule is provided.',
         'protocol'    : '01_start_03_invalidrule.txt',
         },
         {
         'name'        : 'Start - invalid positions',
         'description' : 'Tests if an error-message is printed in case that provided starting positions are invalid (incorrect count of provided fields or unknown fields).',
         'protocol'    : '01_start_04_invalidpositions.txt',
         },
         {
         'name'        : 'Start - invalid positions',
         'description' : 'Tests if an error-message is printed in case that provided starting positions are invalid (incorrect count of tokens on the same field).',
         'protocol'    : '01_start_05_invalidpos.txt',
         },
         {
         'name'        : 'Start - invalid positions',
         'description' : 'Tests if an error-message is printed in case that provided starting positions are invalid (incorrect positions).',
         'protocol'    : '01_start_06_notallowedfield.txt',
         },
         {
         'name'        : 'Start - invalid positions',
         'description' : 'Tests if an error-message is printed in case that provided starting positions are invalid (win situation).',
         'protocol'    : '01_start_07_winpositions.txt',
         },
         {
         'name'        : 'Roll - invalid argument',
         'description' : 'Tests if an error-message is printed in case that the provided argument is invalid (out of range or not a number).',
         'protocol'    : '02_roll_00_invalidargument.txt',
         },
         {
         'name'        : 'Roll - no active game',
         'description' : 'Tests if an error-message is printed in case that the roll command is executed while no game is in process.',
         'protocol'    : '02_roll_01_noactivegame.txt',
         },
         {
         'name'        : 'Roll - move command required',
         'description' : 'Tests if an error-message is printed in case that the roll command is executed when the game is in move phase.',
         'protocol'    : '02_roll_02_moverequired.txt',
         },
         {
         'name'        : 'Move - not existing field',
         'description' : 'Tests if an error-message is printed in case that the move command is executed with a not existing or empty field.',
         'protocol'    : '03_move_00_notexistingfield.txt',
         },
         {
         'name'        : 'Move - not movable',
         'description' : 'Tests if an error-message is printed in case that the move command is executed with a not movable field.',
         'protocol'    : '03_move_01_notmovable.txt',
         },
         {
         'name'        : 'Print - no active game',
         'description' : 'Tests if an error-message is printed in case that the print command is executed while no game is in process.',
         'protocol'    : '04_print_00_noactivegame.txt',
         },
         {
         'name'        : 'Abort - no active game',
         'description' : 'Tests if an error-message is printed in case that the abort command is executed while no game is in process.',
         'protocol'    : '05_abort_00_noactivegame.txt',
         },
		 {
         'name'        : 'Start - invalid configurations',
         'description' : 'Tests if an error-message is printed in case that the start command has invalid input.',
         'protocol'    : '01_start_00_startunordered.txt',
         },
         ]

for test in tests:
    test.setdefault('protocol_code', 'copy/protocols/code.txt')
    test.setdefault('protocol_var', 'copy/protocols/variables.txt')
    test['protocol'] = 'copy/protocols/02_error/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests, excludemain = '', description='These are private tests. We test extended functionality regarding error-handling here.')
sys.exit(0 if success else 1)

