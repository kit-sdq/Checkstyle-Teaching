#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'add existing pupil id',
        'description': 'Tests if adding a pupil with an existing ID is handled correctly.',
        'protocol': 'pupilexistingid.txt',
    },
    {
        'name': 'add pupil without teacher',
        'description': 'Tests if adding a pupil without a teacher is handled correctly.',
        'protocol': 'pupilnoteacher.txt',
    },
    {
        'name': 'submit without assignment',
        'description': 'Tests if submitting without a valid assignment ID is handled correctly.',
        'protocol': 'submitnoassignment.txt',
    },
    {
        'name': 'submit without pupil',
        'description': 'Tests if submitting without a valid pupil ID is handled correctly.',
        'protocol': 'submitnopupil.txt',
    },
    {
        'name': 'submitting more than once',
        'description': 'Tests if submitting more than once is handled correctly.',
        'protocol': 'submitre.txt',
    },
    {
        'name': 'review without assignment',
        'description': 'Tests if reviewing without a valid assignment ID is handled correctly.',
        'protocol': 'reviewnoassignment.txt',
    },
    {
        'name': 'review without pupil',
        'description': 'Tests if reviewing without a valid pupil ID is handled correctly.',
        'protocol': 'reviewnopupil.txt',
    },
    {
        'name': 'reviewing without solution',
        'description': 'Tests if reviewing without solution is handled correctly.',
        'protocol': 'reviewnosubmit.txt',
    },
    {
        'name': 'reviewing with an invalid grade',
        'description': 'Tests if reviewing with an invalid grade is handled correctly.',
        'protocol': 'reviewwronggrade.txt',
    },
    {
        'name': 'list-solutions without assignment',
        'description': 'Tests if list-solutions without a valid assignment ID is handled correctly.',
        'protocol': 'solutionsnoassignment.txt',
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/error_semantic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="Tests if semantical errors are correctly handled.")
sys.exit(0 if success else 1)
