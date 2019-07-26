#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
		'name'        : 'No parameters',
		'description' : 'Tests if the program prints an error if no parameters are given.',
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Empty input file',
		'description' : 'Tests if the program prints an error if the input file is empty.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input_error/empty.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Empty line',
		'description' : 'Tests if the program prints an error if the input file contains an empty line.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input_error/emptyline.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Unknown predicate',
		'description' : 'Tests if the program prints an error if the input file contains an unknown predicate.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input_error/unknown_predicate.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Space in predicate',
		'description' : 'Tests if the program prints an error if the input file contains a predicate with a space in it.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input_error/space_in_predicate.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Illegal product id',
		'description' : 'Tests if the program prints an error if the input file contains an illegal product id.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input_error/illegal_product_id.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Negative product id',
		'description' : 'Tests if the program prints an error if the input file contains a negative product id.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input_error/negative_product_id.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Space in product id',
		'description' : 'Tests if the program prints an error if the input file contains a product id with a space in it.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input_error/space_in_product_id.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Illegal product name',
		'description' : 'Tests if the program prints an error if the input file contains an illegal product name.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input_error/illegal_product_name.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Space in product name',
		'description' : 'Tests if the program prints an error if the input file contains a product name with a space in it.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input_error/space_in_product_name.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Illegal category name',
		'description' : 'Tests if the program prints an error if the input file contains an illegal category name.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input_error/illegal_category_name.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Space in category name',
		'description' : 'Tests if the program prints an error if the input file contains a category name with a space in it.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input_error/space_in_category_name.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Duplicate name, different id',
		'description' : 'Tests if the program prints an error if the input file contains two products with the same name but a different id.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input_error/dup_name_different_id.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Duplicate id, different name',
		'description' : 'Tests if the program prints an error if the input file contains two products with the same id but a different name.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input_error/dup_id_different_name.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'Duplicate name between product and category',
		'description' : 'Tests if the program prints an error if the input file contains a product and a category with the same name.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input_error/dup_name_prod_cat.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'category contained-in product',
		'description' : 'Tests if the program prints an error if the input file contains a category that is contained-in a product.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input_error/cat_containedin_prod.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'product contains category',
		'description' : 'Tests if the program prints an error if the input file contains a product that contains a category.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input_error/prod_contains_cat.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'category source of predecessorof',
		'description' : 'Tests if the program prints an error if the input file contains a category thats the source of a predecessorof relation.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input_error/cat_source_of_pred.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'category target of predecessorof',
		'description' : 'Tests if the program prints an error if the input file contains a category thats the target of a predecessorof relation.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input_error/cat_target_of_pred.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'successorof between two categories',
		'description' : 'Tests if the program prints an error if the input file contains a category connected with another category by a successorof relation.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input_error/succ_two_cat.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
	{
		'name'        : 'two different relations between the same two nodes',
		'description' : 'Tests if the program prints an error if the input file contains a node that is connected by two different relations with another node.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input_error/diff_relations_same_nodes.txt', 'r').read()},
		'protocol'    : 'error.txt',
	},
]

for test in tests:
	test['protocol'] = 'copy/protocols/other_errors/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
						  description="These test correct error handling of command line parameters and the input file.")
sys.exit(0 if success else 1)