#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'add no arguments',
        'description': 'Tests if the add command without arguments is handled correctly.',
        'protocol': 'addnoargs.txt',
    },
    {
        'name': 'add wrong number of arguments',
        'description': 'Tests if the add command with the wrong number of arguments is handled correctly.',
        'protocol': 'addwrongargsnum.txt',
    },
    {
        'name': 'add wrong number of decimals',
        'description': 'Tests if adding a flight with a price with a wrong number of decimals is handled correctly.',
        'protocol': 'addwrongnumdecimals.txt',
    },
    {
        'name': 'add wrong id',
        'description': 'Tests if the add command with a wrong flight number is handled correctly.',
        'protocol': 'addwrongid.txt',
    },
    {
        'name': 'add integer',
        'description': 'Tests if adding a flight with an integer price is handled correctly.',
        'protocol': 'addinteger.txt',
    },
    {
        'name': 'add not a number',
        'description': 'Tests if the add command with a price that is not a number is handled correctly.',
        'protocol': 'addnan.txt',
    },
    {
        'name': 'add currency wrong case',
        'description': 'Tests if adding a flight with the currency in wrong casing is handled correctly.',
        'protocol': 'addcurrencywrongcase.txt',
    },
    {
        'name': 'remove no arguments',
        'description': 'Tests if the remove command without arguments is handled correctly.',
        'protocol': 'removenoargs.txt',
    },
    {
        'name': 'remove wrong number of arguments',
        'description': 'Tests if the remove command with the wrong number of arguments is handled correctly.',
        'protocol': 'removewrongargsnum.txt',
    },
    {
        'name': 'remove wrong id',
        'description': 'Tests if the remove command with a wrong flight number is handled correctly.',
        'protocol': 'removewrongid.txt',
    },
    {
        'name': 'list-flights with arguments',
        'description': 'Tests if the list-flights command with arguments is handled correctly.',
        'protocol': 'lfargs.txt',
    },
    {
        'name': 'book no arguments',
        'description': 'Tests if the book command without arguments is handled correctly.',
        'protocol': 'booknoargs.txt',
    },
    {
        'name': 'book wrong number of arguments',
        'description': 'Tests if the book command with the wrong number of arguments is handled correctly.',
        'protocol': 'bookwrongargsnum.txt',
    },
    {
        'name': 'book wrong id',
        'description': 'Tests if the book command with a wrong flight number is handled correctly.',
        'protocol': 'bookwrongid.txt',
    },
    {
        'name': 'list-bookings with arguments',
        'description': 'Tests if the list-bookings command with arguments is handled correctly.',
        'protocol': 'lbargs.txt',
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
                          description="Tests if semantical errors are correctly handled.")
sys.exit(0 if success else 1)
