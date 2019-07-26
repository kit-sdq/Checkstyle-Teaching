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
        'name': 'Easy',
        'description': 'Tests an easy game without any difficult situations.',
        'protocol': 'easy.txt'
    },
	{
        'name': 'set-vc borders',
        'description': 'Tests if setting vc on borders works correctly.',
        'protocol': 'setvcBorders.txt'
    },
    {
        'name': 'set-vc corners',
        'description': 'Tests if setting vc on corners works correctly.',
        'protocol': 'setvcCorners.txt'
    },
    {
        'name': 'roll ascending',
        'description': 'Tests if all numbers and dawn are rollable ascending (with duplicates).',
        'protocol': 'rollAscending.txt'
    },
    {
        'name': 'roll descending',
        'description': 'Tests if all numbers and dawn are rollable descending (with duplicates).',
        'protocol': 'rollDescending.txt'
    },
    {
        'name': 'Horizontal place',
        'description': 'Tests horizontal placing.',
        'protocol': 'horizontalPlace.txt'
    },
    {
        'name': 'Vertical place',
        'description': 'Tests vertical placing.',
        'protocol': 'verticalPlace.txt'
    },
    {
        'name': 'Horizontal move',
        'description': 'Tests horizontal moving.',
        'protocol': 'horizontalMove.txt'
    },
    {
        'name': 'Vertical move',
        'description': 'Tests vertical moving.',
        'protocol': 'verticalMove.txt'
    },
    {
        'name': 'General move',
        'description': 'Tests moving over corners (the most complex moving).',
        'protocol': 'generalMove.txt'
    },
    {
        'name': 'Border left',
        'description': 'Tests if placing and moving on left border works correctly.',
        'protocol': 'borderLeft.txt'
    },
    {
        'name': 'Border right',
        'description': 'Tests if placing and moving on right border works correctly.',
        'protocol': 'borderRight.txt'
    },
    {
        'name': 'Border up',
        'description': 'Tests if placing and moving on upper border works correctly.',
        'protocol': 'borderUp.txt'
    },
    {
        'name': 'Border down',
        'description': 'Tests if placing and moving on lower border works correctly.',
        'protocol': 'borderDown.txt'
    },
    {
        'name': 'Border corner',
        'description': 'Tests if placing and moving on corners works correctly.',
        'protocol': 'borderCorners.txt'
    },
    {
        'name': 'Dawn over border left',
        'description': 'Tests if placing DAWN stones over left border works correctly.',
        'protocol': 'dawnOverBorderLeft.txt'
    },
    {
        'name': 'Dawn over border right',
        'description': 'Tests if placing DAWN stones over right border works correctly.',
        'protocol': 'dawnOverBorderRight.txt'
    },
    {
        'name': 'Dawn over border up',
        'description': 'Tests if placing DAWN stones over upper border works correctly.',
        'protocol': 'dawnOverBorderUp.txt'
    },
    {
        'name': 'Dawn over border down',
        'description': 'Tests if placing DAWN stones over lower border works correctly.',
        'protocol': 'dawnOverBorderDown.txt'
    },
    {
        'name': 'Dawn over border corner',
        'description': 'Tests if placing DAWN stones over border including a corner works correctly.',
        'protocol': 'dawnOverBorderCorner.txt'
    },
    {
        'name': 'Result different areas',
        'description': 'Tests if result calculation works correctly for V and C in different areas.',
        'protocol': 'resultDifferentAreas.txt'
    },
    {
        'name': 'Result max',
        'description': 'Tests if result calculation works correctly for almost maximized result.',
        'protocol': 'resultMax.txt'
    }
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/logic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if the game logic works.")
sys.exit(0 if success else 1)
