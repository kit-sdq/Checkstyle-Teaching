#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'add-admin username exists',
        'description': 'Tests if the add-admin command handles an existing username correctly.',
        'protocol': 'addadminnameexists.txt',
    },
    {
        'name': 'add-admin wrong username length',
        'description': 'Tests if the add-admin command handles a wrong username length correctly.',
        'protocol': 'addadminnamelength.txt',
    },
    {
        'name': 'add-admin wrong password length',
        'description': 'Tests if the add-admin command handles a wrong password length correctly.',
        'protocol': 'addadminpwlength.txt',
    },
    {
        'name': 'add-admin logged in',
        'description': 'Tests if the add-admin command handles a logged in admin correctly.',
        'protocol': 'addadminloggedin.txt',
    },
    {
        'name': 'login-admin nonexisting username',
        'description': 'Tests if the login-admin command handles a nonexisting username correctly.',
        'protocol': 'loginadminnonexistingname.txt',
    },
    {
        'name': 'login-admin wrong password',
        'description': 'Tests if the login-admin command handles a wrong password correctly.',
        'protocol': 'loginadminwrongpw.txt',
    },
    {
        'name': 'login-admin already logged in',
        'description': 'Tests if the login-admin command handles an already logged in admin correctly.',
        'protocol': 'loginadminloggedin.txt',
    },
    {
        'name': 'logout-admin noone logged in',
        'description': 'Tests if the logout-admin command handles no current login correctly.',
        'protocol': 'logoutadminnologin.txt',
    },
    {
        'name': 'add sports venue noone logged in',
        'description': 'Tests if the add-sports-venue command handles no current login correctly.',
        'protocol': 'addsportsvenuenologin.txt',
    },
    {
        'name': 'add sports venue country not registered',
        'description': 'Tests if the add-sports-venue command handles no IOC registration correctly.',
        'protocol': 'addsportsvenuenoioccode.txt',
    },
    {
        'name': 'add sports venue id exists',
        'description': 'Tests if the add-sports-venue command handles an already existing ID correctly.',
        'protocol': 'addsportsvenueidexists.txt',
    },
    {
        'name': 'add sports venue id not 3 digits',
        'description': 'Tests if the add-sports-venue command handles a wrong ID length correctly.',
        'protocol': 'addsportsvenuewrongidlength.txt',
    },
    {
        'name': 'add sports venue negative id',
        'description': 'Tests if the add-sports-venue command handles a negative ID correctly.',
        'protocol': 'addsportsvenuenegativeid.txt',
    },
    {
        'name': 'add sports venue year not 4 digits',
        'description': 'Tests if the add-sports-venue command handles a wrong year length correctly.',
        'protocol': 'addsportsvenuewrongyearlength.txt',
    },
    {
        'name': 'add sports venue negative year',
        'description': 'Tests if the add-sports-venue command handles negative years correctly.',
        'protocol': 'addsportsvenuenegativeyear.txt',
    },
    {
        'name': 'add sports venue negative seat count',
        'description': 'Tests if the add-sports-venue command handles a negative seat count correctly.',
        'protocol': 'addsportsvenuenegativeseats.txt',
    },
    {
        'name': 'list sports venues noone logged in',
        'description': 'Tests if the list-sports-venues command handles no current login correctly.',
        'protocol': 'listsportsvenuesnologin.txt',
    },
    {
        'name': 'list sports venues country not registered',
        'description': 'Tests if the list-sports-venues command handles unregistered countries correctly.',
        'protocol': 'listsportsvenuesunregistered.txt',
    },
    {
        'name': 'add olympic sport noone logged in',
        'description': 'Tests if the add-olympic-sport command handles no current login correctly.',
        'protocol': 'addolympicsportnologin.txt',
    },
    {
        'name': 'add olympic sport combination exists',
        'description': 'Tests if the add-olympic-sport command handles existing combinations correctly.',
        'protocol': 'addolympicsportexists.txt',
    },
    {
        'name': 'list olympic sports noone logged in',
        'description': 'Tests if the list-olympic-sports command handles no current login correctly.',
        'protocol': 'listolympicsportsnologin.txt',
    },
    {
        'name': 'add ioc code noone logged in',
        'description': 'Tests if the add-ioc-code command handles no current login correctly.',
        'protocol': 'addioccodenologin.txt',
    },
    {
        'name': 'add ioc code id not 3 digits',
        'description': 'Tests if the add-ioc-code command handles wrong ID length correctly.',
        'protocol': 'addioccodewrongidlength.txt',
    },
    {
        'name': 'add ioc code negative id',
        'description': 'Tests if the add-ioc-code command handles negative IDs correctly.',
        'protocol': 'addioccodenegativeid.txt',
    },
    {
        'name': 'add ioc code code not 3 characters',
        'description': 'Tests if the add-ioc-code command handles wrong code length correctly.',
        'protocol': 'addioccodewrongcodelength.txt',
    },
    {
        'name': 'add ioc code existing code',
        'description': 'Tests if the add-ioc-code command handles existing codes correctly.',
        'protocol': 'addioccodeexists.txt',
    },
    {
        'name': 'add ioc code year not 4 digits',
        'description': 'Tests if the add-ioc-code command handles wrong year length correctly.',
        'protocol': 'addioccodewrongyearlength.txt',
    },
    {
        'name': 'add ioc code negative year',
        'description': 'Tests if the add-ioc-code command handles negative years correctly.',
        'protocol': 'addioccodenegativeyear.txt',
    },
    {
        'name': 'list ioc codes noone logged in',
        'description': 'Tests if the list-ioc-codes command handles no current login correctly.',
        'protocol': 'listioccodesnologin.txt',
    },
    {
        'name': 'add athlete noone logged in',
        'description': 'Tests if the add-athlete command handles no current login correctly.',
        'protocol': 'addathletenologin.txt',
    },
    {
        'name': 'add athlete id exists',
        'description': 'Tests if the add-athlete command handles existing IDs without matching name and country correctly.',
        'protocol': 'addathleteidexists.txt',
    },
    {
        'name': 'add athlete no such combination',
        'description': 'Tests if the add-athlete command handles nonexisting combination of sport and discipline correctly.',
        'protocol': 'addathletenosuchcombination.txt',
    },
    {
        'name': 'add athlete country not registered',
        'description': 'Tests if the add-athlete command handles countries without registration correctly.',
        'protocol': 'addathleteunregistered.txt',
    },
    {
        'name': 'summary athletes noone logged in',
        'description': 'Tests if the summary-athletes command handles no current login correctly.',
        'protocol': 'summaryathletesnologin.txt',
    },
    {
        'name': 'summary athletes no such combination',
        'description': 'Tests if the summary-athletes command handles nonexisting combination of sport and discipline correctly.',
        'protocol': 'summaryathletesnosuchcombination.txt',
    },
    {
        'name': 'add competition noone logged in',
        'description': 'Tests if the add-competition command handles no current login correctly.',
        'protocol': 'addcompnologin.txt',
    },
    {
        'name': 'add competition nonexistent id',
        'description': 'Tests if the add-competition command handles nonexistent IDs correctly.',
        'protocol': 'addcompnonexistentid.txt',
    },
    {
        'name': 'add competition year invalid',
        'description': 'Tests if the add-competition command handles invalid years correctly.',
        'protocol': 'addcompinvalidyear.txt',
    },
    {
        'name': 'add competition no such combination',
        'description': 'Tests if the add-competition command handles nonexisting combination of sport and discipline correctly.',
        'protocol': 'addcompnosuchcombination.txt',
    },
    {
        'name': 'add competition athlete doesn\'t participate',
        'description': 'Tests if the add-competition command handles an athlete that doesn\'t participate in that discipline correctly.',
        'protocol': 'addcompnoparticipation.txt',
    },
    {
        'name': 'add competition athlete from different country',
        'description': 'Tests if the add-competition command handles it correctly when the country of the athlete is wrong.',
        'protocol': 'addcompwrongcountry.txt',
    },
    {
        'name': 'add competition wrong medal composition',
        'description': 'Tests if the add-competition command handles wrong medal compositions correctly.',
        'protocol': 'addcompwrongmedalcomp.txt',
    },
    {
        'name': 'add competition already participated',
        'description': 'Tests if the add-competition command handles already participated competitions correctly.',
        'protocol': 'addcompalreadyparticipated.txt',
    },
    {
        'name': 'olympic medal table noone logged in',
        'description': 'Tests if the olympic-medal-table command handles no current login correctly.',
        'protocol': 'omtnologin.txt',
    },
    {
        'name': 'reset noone logged in',
        'description': 'Tests if the reset command handles no current login correctly.',
        'protocol': 'resetnologin.txt',
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/errsemantic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="Tests if semantical errors are correctly handled.")
sys.exit(0 if success else 1)
