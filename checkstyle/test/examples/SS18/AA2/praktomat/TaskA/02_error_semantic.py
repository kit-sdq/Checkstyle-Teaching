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
        'name': 'create existing trie',
        'description': 'Tests if creating an existing trie is handled correctly.',
        'protocol': 'createexistingtrie.txt',
    },
    {
        'name': 'reset nonexistant trie',
        'description': 'Tests if resetting a nonexistant trie is handled correctly.',
        'protocol': 'resetnonexistanttrie.txt',
    },
    {
        'name': 'add nonexistant trie',
        'description': 'Tests if adding to nonexistant trie is handled correctly.',
        'protocol': 'addnonexistanttrie.txt',
    },
    {
        'name': 'add existing name',
        'description': 'Tests if adding an existing name is handled correctly.',
        'protocol': 'addexistingname.txt',
    },
    {
        'name': 'add negative points',
        'description': 'Tests if adding negative points is handled correctly.',
        'protocol': 'addnegativepoints.txt',
    },
    {
        'name': 'add float points',
        'description': 'Tests if adding float points is handled correctly.',
        'protocol': 'addfloatpoints.txt',
    },
    {
        'name': 'modify nonexistant trie',
        'description': 'Tests if modifying in a nonexistant trie is handled correctly.',
        'protocol': 'modifynonexistanttrie.txt',
    },
    {
        'name': 'modify nonexistant name',
        'description': 'Tests if modifying a nonexistant name is handled correctly.',
        'protocol': 'modifynonexistantname.txt',
    },
    {
        'name': 'modify negative points',
        'description': 'Tests if modifying to negative points is handled correctly.',
        'protocol': 'modifynegativepoints.txt',
    },
    {
        'name': 'modify float points',
        'description': 'Tests if modifying to float points is handled correctly.',
        'protocol': 'modifyfloatpoints.txt',
    },
    {
        'name': 'delete nonexistant trie',
        'description': 'Tests if deleting from a nonexistant trie is handled correctly.',
        'protocol': 'deletenonexistanttrie.txt',
    },
    {
        'name': 'delete nonexistant name',
        'description': 'Tests if deleting a nonexistant name is handled correctly.',
        'protocol': 'deletenonexistantname.txt',
    },
    {
        'name': 'modify deleted',
        'description': 'Tests if modifying a deleted name is handled correctly.',
        'protocol': 'modifydeletedname.txt',
    },
    {
        'name': 'credits nonexistant trie',
        'description': 'Tests if reading credits from a nonexistant trie is handled correctly.',
        'protocol': 'creditsnonexistanttrie.txt',
    },
    {
        'name': 'credits nonexistant name',
        'description': 'Tests if reading credits from a nonexistant name is handled correctly.',
        'protocol': 'creditsnonexistantname.txt',
    },
    {
        'name': 'print nonexistant trie',
        'description': 'Tests if printing a nonexistant trie is handled correctly.',
        'protocol': 'printnonexistanttrie.txt',
    },
    {
        'name': 'average nonexistant trie',
        'description': 'Tests if averageing a nonexistant trie is handled correctly.',
        'protocol': 'averagenonexistanttrie.txt',
    },
    {
        'name': 'average no points entered',
        'description': 'Tests if averageing an empty trie is handled correctly.',
        'protocol': 'averageemptytrie.txt',
    },
    {
        'name': 'median nonexistant trie',
        'description': 'Tests if calculating the median of a nonexistant trie is handled correctly.',
        'protocol': 'mediannonexistanttrie.txt',
    },
    {
        'name': 'median no points entered',
        'description': 'Tests if calculating the median of an empty trie is handled correctly.',
        'protocol': 'medianemptytrie.txt',
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/error_semantic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if logical and semantic errors are handled correctly.")
sys.exit(0 if success else 1)
