#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
		'name'        : 'Add negative ints',
		'description' : 'Tests if the adding negative ints is working.',
		'protocol'    : 'addNegative.txt',
	},
	{
		'name'        : 'Size',
		'description' : 'Tests if the size command is working.',
		'protocol'    : 'size.txt',
	},
		{
		'name'        : 'IsEmpty',
		'description' : 'Tests if the isEmpty command is working.',
		'protocol'    : 'isEmpty.txt',
	},
	{
		'name'        : 'Clear',
		'description' : 'Tests if the clear command is working.',
		'protocol'    : 'clear.txt',
	},
	{
		'name'        : 'Get',
		'description' : 'Tests if the get command is working.',
		'protocol'    : 'get.txt',
	},
	{
		'name'        : 'IndexOf',
		'description' : 'Tests if the indexOf command is working.',
		'protocol'    : 'indexOf.txt',
	},
	{
		'name'        : 'IndexOfMultipleOccurencies',
		'description' : 'Tests if the indexOf command is working correctly with multiple occurencies.',
		'protocol'    : 'indexOfMultipleOccurencies.txt',
	},
	{
		'name'        : 'LastIndexOf',
		'description' : 'Tests if the lastIndexOf command is working.',
		'protocol'    : 'lastIndexOf.txt',
	},
	{
		'name'        : 'LastIndexOfMultipleOccurencies',
		'description' : 'Tests if the lastIndexOf command is working correctly with multiple occurencies.',
		'protocol'    : 'lastIndexOfMultipleOccurencies.txt',
	},
	{
		'name'        : 'Remove',
		'description' : 'Tests if the remove command is working.',
		'protocol'    : 'remove.txt',
	},
	{
		'name'        : 'Contains',
		'description' : 'Tests if the contains command is working.',
		'protocol'    : 'contains.txt',
	},
	{
		'name'        : 'After Clear',
		'description' : 'Tests if the clear command results in an empty list.',
		'protocol'    : 'afterClear.txt',
	},
	{
		'name'        : 'After Remove',
		'description' : 'Tests if the remove command removes the elements.',
		'protocol'    : 'afterRemove.txt',
	},
	{
		'name'        : 'First example interaction',
		'description' : 'Tests if the first given example interaction is working.',
		'protocol'    : 'firstExampleInteraction.txt',
	},
	{
		'name'        : 'Second example interaction',
		'description' : 'Tests if the second given example interaction is working.',
		'protocol'    : 'secondExampleInteraction.txt',
	}
]

for test in tests:
	test['protocol'] = 'copy/protocols/private/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
						  description="Tests if your program works correctly.")
sys.exit(0 if success else 1)
