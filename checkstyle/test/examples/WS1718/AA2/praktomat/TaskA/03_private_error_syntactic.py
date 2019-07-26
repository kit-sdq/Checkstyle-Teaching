#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'add-admin no arguments',
        'description': 'Tests if the add-admin command without arguments is handled correctly.',
        'protocol': 'addadminnoargs.txt',
    },
    {
        'name': 'add-admin wrong number of arguments',
        'description': 'Tests if the add-admin command with a wrong number of arguments is handled correctly.',
        'protocol': 'addadminwrongargsnum.txt',
    },
    {
        'name': 'add-admin illegal arguments',
        'description': 'Tests if the add-admin command with illegal arguments is handled correctly.',
        'protocol': 'addadminillegalargs.txt',
    },
    {
        'name': 'login-admin no arguments',
        'description': 'Tests if the login-admin command without arguments is handled correctly.',
        'protocol': 'loginadminnoargs.txt',
    },
    {
        'name': 'login-admin wrong number of arguments',
        'description': 'Tests if the login-admin command with a wrong number of arguments is handled correctly.',
        'protocol': 'loginadminwrongargsnum.txt',
    },
    {
        'name': 'login-admin illegal arguments',
        'description': 'Tests if the login-admin command with illegal arguments is handled correctly.',
        'protocol': 'loginadminillegalargs.txt',
    },
    {
        'name': 'logout-admin with arguments',
        'description': 'Tests if the logout-admin command with arguments is handled correctly.',
        'protocol': 'logoutadminargs.txt',
    },
    {
        'name': 'add-sports-venue no arguments',
        'description': 'Tests if the add-sports-venue command without arguments is handled correctly.',
        'protocol': 'addsportsvenuenoargs.txt',
    },
    {
        'name': 'add-sports-venue wrong number of arguments',
        'description': 'Tests if the add-sports-venue command with a wrong number of arguments is handled correctly.',
        'protocol': 'addsportsvenuewrongargsnum.txt',
    },
    {
        'name': 'add-sports-venue illegal arguments',
        'description': 'Tests if the add-sports-venue command with illegal arguments is handled correctly.',
        'protocol': 'addsportsvenueillegalargs.txt',
    },
    {
        'name': 'list-sports-venues no arguments',
        'description': 'Tests if the list-sports-venues command without arguments is handled correctly.',
        'protocol': 'listsportsvenuesnoargs.txt',
    },
    {
        'name': 'add-olympic-sport no arguments',
        'description': 'Tests if the add-olympic-sport command without arguments is handled correctly.',
        'protocol': 'addolympicsportnoargs.txt',
    },
    {
        'name': 'add-olympic-sport wrong number of arguments',
        'description': 'Tests if the add-olympic-sport command with a wrong number of arguments is handled correctly.',
        'protocol': 'addolympicsportwrongargsnum.txt',
    },
    {
        'name': 'add-olympic-sport illegal arguments',
        'description': 'Tests if the add-olympic-sport command with illegal arguments is handled correctly.',
        'protocol': 'addolympicsportillegalargs.txt',
    },
    {
        'name': 'list-olympic-sports with arguments',
        'description': 'Tests if the list-olympic-sports command with arguments is handled correctly.',
        'protocol': 'listolympicsportsargs.txt',
    },
    {
        'name': 'add-ioc-code no arguments',
        'description': 'Tests if the add-ioc-code command without arguments is handled correctly.',
        'protocol': 'addioccodenoargs.txt',
    },
    {
        'name': 'add-ioc-code wrong number of arguments',
        'description': 'Tests if the add-ioc-code command with a wrong number of arguments is handled correctly.',
        'protocol': 'addioccodewrongargsnum.txt',
    },
    {
        'name': 'add-ioc-code illegal arguments',
        'description': 'Tests if the add-ioc-code command with illegal arguments is handled correctly.',
        'protocol': 'addioccodeillegalargs.txt',
    },
    {
        'name': 'list-ioc-codes with arguments',
        'description': 'Tests if the list-ioc-codes command with arguments is handled correctly.',
        'protocol': 'listioccodesargs.txt',
    },
    {
        'name': 'add-athlete no arguments',
        'description': 'Tests if the add-athlete command without arguments is handled correctly.',
        'protocol': 'addathletenoargs.txt',
    },
    {
        'name': 'add-athlete wrong number of arguments',
        'description': 'Tests if the add-athlete command with a wrong number of arguments is handled correctly.',
        'protocol': 'addathletewrongargsnum.txt',
    },
    {
        'name': 'add-athlete illegal arguments',
        'description': 'Tests if the add-athlete command with illegal arguments is handled correctly.',
        'protocol': 'addathleteillegalargs.txt',
    },
    {
        'name': 'summary-athletes no arguments',
        'description': 'Tests if the summary-athletes command without arguments is handled correctly.',
        'protocol': 'summaryathletesnoargs.txt',
    },
    {
        'name': 'summary-athletes wrong number of arguments',
        'description': 'Tests if the summary-athletes command with a wrong number of arguments is handled correctly.',
        'protocol': 'summaryathleteswrongargsnum.txt',
    },
    {
        'name': 'summary-athletes illegal arguments',
        'description': 'Tests if the summary-athletes command with illegal arguments is handled correctly.',
        'protocol': 'summaryathletesillegalargs.txt',
    },
    {
        'name': 'add-competition no arguments',
        'description': 'Tests if the add-competition command without arguments is handled correctly.',
        'protocol': 'addcompetitionnoargs.txt',
    },
    {
        'name': 'add-competition wrong number of arguments',
        'description': 'Tests if the add-competition command with a wrong number of arguments is handled correctly.',
        'protocol': 'addcompetitionwrongargsnum.txt',
    },
    {
        'name': 'add-competition illegal arguments',
        'description': 'Tests if the add-competition command with illegal arguments is handled correctly.',
        'protocol': 'addcompetitionillegalargs.txt',
    },
    {
        'name': 'olympic-medal-table with arguments',
        'description': 'Tests if the olympic-medal-table command with arguments is handled correctly.',
        'protocol': 'olympicmedaltableargs.txt',
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
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/errsyntactic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="Tests if syntactical errors are correctly handled.")
sys.exit(0 if success else 1)
