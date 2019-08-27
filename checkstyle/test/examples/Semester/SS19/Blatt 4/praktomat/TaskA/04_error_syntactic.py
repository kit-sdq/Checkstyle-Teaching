#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'Not enough cmd arguments',
        'description': 'Tests if not enough arguments work correctly.',
        'protocol': 'error.txt',
		'arguments': 'standard',
    },
	{
        'name': 'Too many cmd arguments',
        'description': 'Tests if to many arguments work correctly.',
        'protocol': 'error.txt',
		'arguments': 'standard 5 f',
    },
	{
        'name': 'Invalid mode',
        'description': 'Tests if an invalid mode is detected correctly.',
        'protocol': 'error.txt',
		'arguments': 'standards 5',
    },
	{
        'name': 'Invalid size',
        'description': 'Tests if an invalid size is detected correctly.',
        'protocol': 'error.txt',
		'arguments': 'standard 6',
    },
	{
        'name': 'Invalid size 2',
        'description': 'Tests if an invalid size is detected correctly.',
        'protocol': 'error.txt',
		'arguments': 'standard 8',
    },
	
	#start
	{
        'name': 'start no args',
        'description': 'Tests if no arguments are detected correctly.',
        'protocol': 'startnoargs.txt',
		'arguments': 'standard 5',
    },
	{
        'name': 'start invalid args',
        'description': 'Tests if invalid arguments are detected correctly.',
        'protocol': 'startwrongargsformat.txt',
		'arguments': 'standard 5',
    },
	{
        'name': 'start invalid args number',
        'description': 'Tests if an invalid number of arguments is detected correctly.',
        'protocol': 'startwrongargsnum.txt',
		'arguments': 'standard 5',
    },
	
	# roll
	{
        'name': 'roll no args',
        'description': 'Tests if no arguments are detected correctly.',
        'protocol': 'rollnoargs.txt',
		'arguments': 'standard 5',
    },
	{
        'name': 'roll invalid args',
        'description': 'Tests if invalid arguments are detected correctly.',
        'protocol': 'rollwrongargsformat.txt',
		'arguments': 'standard 5',
    },
	{
        'name': 'roll invalid args number',
        'description': 'Tests if an invalid number of arguments is detected correctly.',
        'protocol': 'rollwrongargsnum.txt',
		'arguments': 'standard 5',
    },
	
	# move
	{
        'name': 'move no args',
        'description': 'Tests if no arguments are detected correctly.',
        'protocol': 'movenoargs.txt',
		'arguments': 'standard 5',
    },
	{
        'name': 'move invalid args',
        'description': 'Tests if invalid arguments are detected correctly.',
        'protocol': 'movewrongargsformat.txt',
		'arguments': 'standard 5',
    },
	{
        'name': 'move invalid args number',
        'description': 'Tests if an invalid number of arguments is detected correctly.',
        'protocol': 'movewrongargsnum.txt',
		'arguments': 'standard 5',
    },
	
	# token
	{
        'name': 'token no args',
        'description': 'Tests if no arguments are detected correctly.',
        'protocol': 'tokennoargs.txt',
		'arguments': 'standard 5',
    },
	{
        'name': 'token invalid args',
        'description': 'Tests if invalid arguments are detected correctly.',
        'protocol': 'tokenwrongargsformat.txt',
		'arguments': 'standard 5',
    },
	{
        'name': 'token invalid args number',
        'description': 'Tests if an invalid number of arguments is detected correctly.',
        'protocol': 'tokenwrongargsnum.txt',
		'arguments': 'standard 5',
    },
	
	# print
	{
        'name': 'print no args',
        'description': 'Tests if no arguments are detected correctly.',
        'protocol': 'printnoargs.txt',
		'arguments': 'standard 5',
    },
	{
        'name': 'print invalid args',
        'description': 'Tests if invalid arguments are detected correctly.',
        'protocol': 'printwrongargsformat.txt',
		'arguments': 'standard 5',
    },
	{
        'name': 'print invalid args number',
        'description': 'Tests if an invalid number of arguments is detected correctly.',
        'protocol': 'printwrongargsnum.txt',
		'arguments': 'standard 5',
    },
	
	# print-cell
	{
        'name': 'print-cell no args',
        'description': 'Tests if no arguments are detected correctly.',
        'protocol': 'printcellnoargs.txt',
		'arguments': 'standard 5',
    },
	{
        'name': 'print-cell invalid args',
        'description': 'Tests if invalid arguments are detected correctly.',
        'protocol': 'printcellwrongargsformat.txt',
		'arguments': 'standard 5',
    },
	{
        'name': 'print-cell invalid args number',
        'description': 'Tests if an invalid number of arguments is detected correctly.',
        'protocol': 'printcellwrongargsnum.txt',
		'arguments': 'standard 5',
    },
	
	# quit
	{
        'name': 'quit invalid args',
        'description': 'Tests if invalid arguments are detected correctly.',
        'protocol': 'quitargs.txt',
		'arguments': 'standard 5',
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/error_syntactic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="Tests if semantical errors are correctly handled.")
sys.exit(0 if success else 1)
