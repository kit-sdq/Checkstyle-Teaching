#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
		'name'        : 'Product with spaces',
		'description' : 'Tests if the public test works if the product contains spaces.',
		'protocol'    : 'spaces.txt',
	},
	{
		'name'        : 'One order of more than one product',
		'description' : 'Tests if one order with one customer and one item works correctly when the amount is higher than one.',
		'protocol'    : 'amountGreaterOne.txt',
	},
	{
		'name'        : 'Multiple orders, one customer, one item',
		'description' : 'Tests if multiple orders with one customer and one item works correctly.',
		'protocol'    : 'multipleOrders.txt',
	},
	{
		'name'        : 'Multiple orders, multiple customers, one item',
		'description' : 'Tests if multiple orders with multiple customer and one item works correctly.',
		'protocol'    : 'multipleCustomers.txt',
	},
	{
		'name'        : 'Example',
		'description' : 'Tests if the assignment sheet example works correctly.',
		'protocol'    : 'example.txt',
	},
	{
		'name'        : 'Multiple orders, multiple customers, multiple items',
		'description' : 'Tests if multiple orders with multiple customer and multiple items works correctly.',
		'protocol'    : 'multipleEverything.txt',
	}
]

for test in tests:
	test['protocol'] = 'copy/protocols/private/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
						  description="This tests more complex command inputs.")
sys.exit(0 if success else 1)
