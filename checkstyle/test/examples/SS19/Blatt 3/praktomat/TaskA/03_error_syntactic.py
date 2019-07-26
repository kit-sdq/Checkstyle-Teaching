#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'teacher no arguments',
        'description': 'Tests if the teacher command without arguments is handled correctly.',
        'protocol': 'teachernoargs.txt',
    },
    {
        'name': 'teacher wrong number of arguments',
        'description': 'Tests if the teacher command with the wrong number of arguments is handled correctly.',
        'protocol': 'teacherwrongargsnum.txt',
    },
    {
        'name': 'teacher wrong argument format',
        'description': 'Tests if the teacher command with a wrong format of arguments is handled correctly.',
        'protocol': 'teacherwrongargsformat.txt',
    },
    {
        'name': 'pupil no arguments',
        'description': 'Tests if the pupil command without arguments is handled correctly.',
        'protocol': 'pupilnoargs.txt',
    },
    {
        'name': 'pupil wrong number of arguments',
        'description': 'Tests if the pupil command with the wrong number of arguments is handled correctly.',
        'protocol': 'pupilwrongargsnum.txt',
    },
    {
        'name': 'pupil wrong argument format',
        'description': 'Tests if the pupil command with a wrong format of arguments is handled correctly.',
        'protocol': 'pupilwrongargsformat.txt',
    },
    {
        'name': 'assignment no arguments',
        'description': 'Tests if the assignment command without arguments is handled correctly.',
        'protocol': 'assignmentnoargs.txt',
    },
    {
        'name': 'assignment wrong number of arguments',
        'description': 'Tests if the assignment command with the wrong number of arguments is handled correctly.',
        'protocol': 'assignmentwrongargsnum.txt',
    },
    {
        'name': 'assignment wrong argument format',
        'description': 'Tests if the assignment command with a wrong format of arguments is handled correctly.',
        'protocol': 'assignmentwrongargsformat.txt',
    },
    {
        'name': 'submit no arguments',
        'description': 'Tests if the submit command without arguments is handled correctly.',
        'protocol': 'submitnoargs.txt',
    },
    {
        'name': 'submit wrong number of arguments',
        'description': 'Tests if the submit command with the wrong number of arguments is handled correctly.',
        'protocol': 'submitwrongargsnum.txt',
    },
    {
        'name': 'submit wrong argument format',
        'description': 'Tests if the submit command with a wrong format of arguments is handled correctly.',
        'protocol': 'submitwrongargsformat.txt',
    },
    {
        'name': 'review no arguments',
        'description': 'Tests if the review command without arguments is handled correctly.',
        'protocol': 'reviewnoargs.txt',
    },
    {
        'name': 'review wrong number of arguments',
        'description': 'Tests if the review command with the wrong number of arguments is handled correctly.',
        'protocol': 'reviewwrongargsnum.txt',
    },
    {
        'name': 'review wrong argument format',
        'description': 'Tests if the review command with a wrong format of arguments is handled correctly.',
        'protocol': 'reviewwrongargsformat.txt',
    },
    {
        'name': 'list-solutions no arguments',
        'description': 'Tests if the list-solutions command without arguments is handled correctly.',
        'protocol': 'solutionsnoargs.txt',
    },
    {
        'name': 'list-solutions wrong number of arguments',
        'description': 'Tests if the list-solutions command with the wrong number of arguments is handled correctly.',
        'protocol': 'solutionswrongargsnum.txt',
    },
    {
        'name': 'list-solutions wrong argument format',
        'description': 'Tests if the list-solutions command with a wrong format of arguments is handled correctly.',
        'protocol': 'solutionswrongargsformat.txt',
    },
    {
        'name': 'list-pupils with arguments',
        'description': 'Tests if the list-pupils command with arguments is handled correctly.',
        'protocol': 'lpargs.txt',
    },
    {
        'name': 'results with arguments',
        'description': 'Tests if the results command with arguments is handled correctly.',
        'protocol': 'rargs.txt',
    },
    {
        'name': 'summary-assignment with arguments',
        'description': 'Tests if the summary-assignment command with arguments is handled correctly.',
        'protocol': 'saargs.txt',
    },
    {
        'name': 'summary-teacher with arguments',
        'description': 'Tests if the summary-teacher command with arguments is handled correctly.',
        'protocol': 'stargs.txt',
    },
    {
        'name': 'reset with arguments',
        'description': 'Tests if the reset command with arguments is handled correctly.',
        'protocol': 'resetargs.txt',
    },
    {
        'name': 'quit with arguments',
        'description': 'Tests if the quit command with arguments is handled correctly.',
        'protocol': 'quitargs.txt',
    }
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/error_syntactic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="Tests if semantical errors are correctly handled.")
sys.exit(0 if success else 1)
