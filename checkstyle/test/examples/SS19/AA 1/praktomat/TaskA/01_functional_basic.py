#!/usr/bin/env python
# -*- encoding: utf-8 -*-
import logging
import os

if not(os.environ.get('PYTHOMAT_TESTS_LOG')):
	log_file = os.path.join(os.path.dirname(os.path.abspath(__file__)), "tests.log")
	os.environ['PYTHOMAT_TESTS_LOG'] = log_file

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	# Addition
    {
        'name': 'Basic addition',
        'description': 'Tests if basic adition works.',
        'protocol': 'A_basicAddition.txt',
    },
    {
        'name': 'Basic addition with variables',
        'description': 'Tests if addition with variables works.',
        'protocol': 'A_basicAdditionWithVariables.txt',
    },
	{
        'name': 'Basic addition with negative numbers',
        'description': 'Tests if addition with negative numbers works.',
        'protocol': 'A_basicAdditionWithNegative.txt',
    },
	{
        'name': 'Basic addition with negative variables',
        'description': 'Tests if addition with negative variables works.',
        'protocol': 'A_basicAdditionWithVariablesNegative.txt',
    },
	{
        'name': 'Complex addition',
        'description': 'Tests if complex adition works.',
        'protocol': 'A_complexAddition.txt',
    },
    {
        'name': 'Complex addition with variables',
        'description': 'Tests if addition with variables works.',
        'protocol': 'A_complexAdditionWithVariables.txt',
    },
	{
        'name': 'Complex addition with negative numbers',
        'description': 'Tests if addition with negative complex numbers works.',
        'protocol': 'A_complexAdditionWithNegative.txt',
    },
	{
        'name': 'Complex addition with negative variables',
        'description': 'Tests if addition with negative variables works.',
        'protocol': 'A_complexAdditionWithVariablesNegative.txt',
    },
	{
        'name': 'Basic assign',
        'description': 'Tests if assigning a value works.',
        'protocol': 'Z_basicAssign.txt',
    },
	{
        'name': 'Basic assign with negative number',
        'description': 'Tests if assigning a negative value works.',
        'protocol': 'Z_basicAssignWithNegative.txt',
    },
	
	
	
	# Division
	{
        'name': 'Basic division',
        'description': 'Tests if basic division works.',
        'protocol': 'D_basicDiv.txt',
    },
    {
        'name': 'Basic division with variables',
        'description': 'Tests if division with variables works.',
        'protocol': 'D_basicDivWithVariables.txt',
    },
	{
        'name': 'Basic division with negative numbers',
        'description': 'Tests if division with negative numbers works.',
        'protocol': 'D_basicDivWithNegative.txt',
    },
	{
        'name': 'Basic division with negative variables',
        'description': 'Tests if division with negative variables works.',
        'protocol': 'D_basicDivWithVariablesNegative.txt',
    },
	{
        'name': 'Complex division',
        'description': 'Tests if complex division works.',
        'protocol': 'D_complexDiv.txt',
    },
    {
        'name': 'Complex division with variables',
        'description': 'Tests if division with variables works.',
        'protocol': 'D_complexDivWithVariables.txt',
    },
	{
        'name': 'Complex division with negative numbers',
        'description': 'Tests if division with negative complex numbers works.',
        'protocol': 'D_complexDivWithNegative.txt',
    },
	{
        'name': 'Complex division with negative variables',
        'description': 'Tests if division with negative variables works.',
        'protocol': 'D_complexDivWithVariablesNegative.txt',
    },
	
	
		# Multiplication
	{
        'name': 'Basic multiplication',
        'description': 'Tests if basic multiplication works.',
        'protocol': 'M_basicMul.txt',
    },
    {
        'name': 'Basic multiplication with variables',
        'description': 'Tests if multiplication with variables works.',
        'protocol': 'M_basicMulWithVariables.txt',
    },
	{
        'name': 'Basic multiplication with negative numbers',
        'description': 'Tests if multiplication with negative numbers works.',
        'protocol': 'M_basicMulWithNegative.txt',
    },
	{
        'name': 'Basic multiplication with negative variables',
        'description': 'Tests if multiplication with negative variables works.',
        'protocol': 'M_basicMulWithVariablesNegative.txt',
    },
	{
        'name': 'Complex multiplication',
        'description': 'Tests if complex multiplication works.',
        'protocol': 'M_complexMul.txt',
    },
    {
        'name': 'Complex multiplication with variables',
        'description': 'Tests if multiplication with variables works.',
        'protocol': 'M_complexMulWithVariables.txt',
    },
	{
        'name': 'Complex multiplication with negative numbers',
        'description': 'Tests if multiplication with negative complex numbers works.',
        'protocol': 'M_complexMulWithNegative.txt',
    },
	{
        'name': 'Complex multiplication with negative variables',
        'description': 'Tests if multiplication with negative variables works.',
        'protocol': 'M_complexMulWithVariablesNegative.txt',
    },
	
	
	
		# Subtraction
	{
        'name': 'Basic subtraction',
        'description': 'Tests if basic subtraction works.',
        'protocol': 'S_basicSub.txt',
    },
    {
        'name': 'Basic subtraction with variables',
        'description': 'Tests if subtraction with variables works.',
        'protocol': 'S_basicSubWithVariables.txt',
    },
	{
        'name': 'Basic subtraction with negative numbers',
        'description': 'Tests if subtraction with negative numbers works.',
        'protocol': 'S_basicSubWithNegative.txt',
    },
	{
        'name': 'Basic subtraction with negative variables',
        'description': 'Tests if subtraction with negative variables works.',
        'protocol': 'S_basicSubWithVariablesNegative.txt',
    },
	{
        'name': 'Complex subtraction',
        'description': 'Tests if complex subtraction works.',
        'protocol': 'S_complexSub.txt',
    },
    {
        'name': 'Complex subtraction with variables',
        'description': 'Tests if subtraction with variables works.',
        'protocol': 'S_complexSubWithVariables.txt',
    },
	{
        'name': 'Complex subtraction with negative numbers',
        'description': 'Tests if subtraction with negative complex numbers works.',
        'protocol': 'S_complexSubWithNegative.txt',
    },
	{
        'name': 'Complex subtraction with negative variables',
        'description': 'Tests if subtraction with negative variables works.',
        'protocol': 'S_complexSubWithVariablesNegative.txt',
    },
	
	
	
	
	
	# assign command
	{
        'name': 'Basic assign',
        'description': 'Tests if assigning a value works.',
        'protocol': 'Z_basicAssign.txt',
    },
	{
        'name': 'Basic assign with negative number',
        'description': 'Tests if assigning a negative value works.',
        'protocol': 'Z_basicAssignWithNegative.txt',
    },
	{
        'name': 'Complex assign',
        'description': 'Tests if assigning a value works.',
        'protocol': 'Z_complexAssign.txt',
    },
	{
        'name': 'Complex assign with negative number',
        'description': 'Tests if assigning a neative complex value works.',
        'protocol': 'Z_complexAssignWithNegative.txt',
    },
	{
        'name': 'Complex assign with negative number',
        'description': 'Tests if assigning a neative complex value works.',
        'protocol': 'Z_complexAssignWithNegative.txt',
    },
	{
        'name': 'Variable command',
        'description': 'Tests if the variable command works.',
        'protocol': '0V_basicVariable.txt',
    },

	
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/functional_basic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if the functionality is given according to the specifications without error handling.")
sys.exit(0 if success else 1)
