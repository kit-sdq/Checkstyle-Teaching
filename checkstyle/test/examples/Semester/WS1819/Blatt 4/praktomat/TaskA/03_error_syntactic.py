#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'add-addressbook no arguments',
        'description': 'Tests if the add-addressbook command without arguments is handled correctly.',
        'protocol': 'addbooknoargs.txt',
    },
    {
        'name': 'add-addressbook wrong number of arguments',
        'description': 'Tests if the add-addressbook command with the wrong number of arguments is handled correctly.',
        'protocol': 'addbookwrongargsnum.txt',
    },
    {
        'name': 'add-addressbook wrong format of argument',
        'description': 'Tests if the add-addressbook command with the wrong format of argument is handled correctly.',
        'protocol': 'addbookwrongargs.txt',
    },
    {
        'name': 'remove-addressbook no arguments',
        'description': 'Tests if the remove-addressbook command without arguments is handled correctly.',
        'protocol': 'removebooknoargs.txt',
    },
    {
        'name': 'remove-addressbook wrong number of arguments',
        'description': 'Tests if the remove-addressbook command with the wrong number of arguments is handled correctly.',
        'protocol': 'removebookwrongargsnum.txt',
    },
    {
        'name': 'remove-addressbook wrong format of argument',
        'description': 'Tests if the remove-addressbook command with the wrong format of argument is handled correctly.',
        'protocol': 'removebookwrongargs.txt',
    },
    {
        'name': 'add-contact no arguments',
        'description': 'Tests if the add-contact command without arguments is handled correctly.',
        'protocol': 'addcontactnoargs.txt',
    },
    {
        'name': 'add-contact wrong number of arguments',
        'description': 'Tests if the add-contact command with the wrong number of arguments is handled correctly.',
        'protocol': 'addcontactwrongargsnum.txt',
    },
    {
        'name': 'add-contact wrong format of argument',
        'description': 'Tests if the add-contact command with the wrong format of argument is handled correctly.',
        'protocol': 'addcontactwrongargs.txt',
    },
    {
        'name': 'remove-contact no arguments',
        'description': 'Tests if the remove-contact command without arguments is handled correctly.',
        'protocol': 'removecontactnoargs.txt',
    },
    {
        'name': 'remove-contact wrong number of arguments',
        'description': 'Tests if the remove-contact command with the wrong number of arguments is handled correctly.',
        'protocol': 'removecontactwrongargsnum.txt',
    },
    {
        'name': 'remove-contact wrong format of argument',
        'description': 'Tests if the remove-contact command with the wrong format of argument is handled correctly.',
        'protocol': 'removecontactwrongargs.txt',
    },
    {
        'name': 'print-addressbook no arguments',
        'description': 'Tests if the print-addressbook command without arguments is handled correctly.',
        'protocol': 'printbooknoargs.txt',
    },
    {
        'name': 'print-addressbook wrong number of arguments',
        'description': 'Tests if the print-addressbook command with the wrong number of arguments is handled correctly.',
        'protocol': 'printbookwrongargsnum.txt',
    },
    {
        'name': 'print-addressbook wrong format of argument',
        'description': 'Tests if the print-addressbook command with the wrong format of argument is handled correctly.',
        'protocol': 'printbookwrongargs.txt',
    },
    {
        'name': 'quit with arguments',
        'description': 'Tests if the quit command with arguments is handled correctly.',
        'protocol': 'quitargs.txt',
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/error_syntactic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="Tests if syntactical errors are correctly handled.")
sys.exit(0 if success else 1)
