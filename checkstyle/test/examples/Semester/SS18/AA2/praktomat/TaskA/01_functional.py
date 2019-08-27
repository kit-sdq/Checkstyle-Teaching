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
    {
        'name': 'reset empty trie',
        'description': 'Tests if resetting an empty trie works correctly.',
        'protocol': 'resetempty.txt',
    },
    {
        'name': 'add zero',
        'description': 'Tests if adding zero points works correctly.',
        'protocol': 'addzero.txt',
    },
    {
        'name': 'add same points',
        'description': 'Tests if adding the points twice works correctly.',
        'protocol': 'addsamepoints.txt',
    },
    {
        'name': 'add/print sorting',
        'description': 'Tests if adding in an unsorted manner works correctly.',
        'protocol': 'addunsorted.txt',
    },
    {
        'name': 'print multiple leafs',
        'description': 'Tests if printing a trie with multiple leafs splitting from a direct parent works correctly.',
        'protocol': 'printmultipleleafs.txt',
    },
    {
        'name': 'add/print multiple leafs unsorted',
        'description': 'Tests if printing a trie with multiple leafs (that got added in an unsorted manner) splitting from a direct parent works correctly.',
        'protocol': 'multileafunsorted.txt',
    },
    {
        'name': 'Create and add figure 1 example',
        'description': 'Tests if the example in figure 1 on the assignment sheet is created correctly.',
        'protocol': 'createfigure1.txt',
    },
    {
        'name': 'credits figure 1 example',
        'description': 'Tests if the credit command works correctly with the example in figure 1 on the assignment sheet.',
        'protocol': 'creditsfigure1.txt',
    },
    {
        'name': 'modify to/from zero',
        'description': 'Tests if modifying points to/from zero works correctly.',
        'protocol': 'modifyzero.txt',
    },
    {
        'name': 'delete',
        'description': 'Tests if the delete command works correctly.',
        'protocol': 'delete.txt',
    },
    {
        'name': 'readd deleted',
        'description': 'Tests if the readding a deleted entry works correctly.',
        'protocol': 'readd.txt',
    },
    {
        'name': 'average only one value',
        'description': 'Tests if the average command with only one value works correctly.',
        'protocol': 'averageonlyoneval.txt',
    },
    {
        'name': 'average only zero',
        'description': 'Tests if the average command with only zero points works correctly.',
        'protocol': 'averagezero.txt',
    },
    {
        'name': 'average non-whole number result',
        'description': 'Tests if the average command works correctly if the result is not a whole number.',
        'protocol': 'averagenonwhole.txt',
    },
    {
        'name': 'average after delete',
        'description': 'Tests if the average command after a delete works correctly.',
        'protocol': 'averagedelete.txt',
    },
    {
        'name': 'average figure 1',
        'description': 'Tests if the average command works correctly with the example in figure 1 on the assignment sheet.',
        'protocol': 'averagefigure1.txt',
    },
    {
        'name': 'median',
        'description': 'Tests if the median command works correctly.',
        'protocol': 'median.txt',
    },
    {
        'name': 'median only zero',
        'description': 'Tests if the median command with only zero points works correctly.',
        'protocol': 'medianzero.txt',
    },
    {
        'name': 'median non-whole number result',
        'description': 'Tests if the median command works correctly if the result is not a whole number.',
        'protocol': 'mediannonwhole.txt',
    },
    {
        'name': 'median after modify',
        'description': 'Tests if the median command after a modify works correctly.',
        'protocol': 'medianmodify.txt',
    },
    {
        'name': 'median after delete',
        'description': 'Tests if the median command after a modify works correctly.',
        'protocol': 'mediandelete.txt',
    },
    {
        'name': 'median figure 1',
        'description': 'Tests if the median command works correctly with the example in figure 1 on the assignment sheet.',
        'protocol': 'medianfigure1.txt',
    },
    {
        'name': 'commands with multiple tries',
        'description': 'Tests if the commands work correctly with multiple tries (without affecting the wrong trie).',
        'protocol': 'multiple.txt',
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/functional/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if the functionality is given according to the specifications without error handling.")
sys.exit(0 if success else 1)
