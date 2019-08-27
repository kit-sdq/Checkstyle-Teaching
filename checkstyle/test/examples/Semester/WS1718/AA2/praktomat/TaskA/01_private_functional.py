#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'add admin special',
        'description': 'Tests if the add-admins command works correctly for special cases.',
        'protocol': 'addadminspecial.txt',
    },
    {
        'name': 'add admin same name',
        'description': 'Tests if the add-admins command works correctly when two admins have the same name.',
        'protocol': 'addadminsamename.txt',
    },
    {
        'name': 'add admin same password',
        'description': 'Tests if the add-admins command works correctly when two admins have the same password.',
        'protocol': 'addadminsamepw.txt',
    },
    {
        'name': 'logout admin',
        'description': 'Tests if the logout-admin command works correctly.',
        'protocol': 'logoutadmin.txt',
    },
    {
        'name': 'admin commands interaction',
        'description': 'Tests if the adding, login and logout work correctly with multiple accounts interactively.',
        'protocol': 'admininteraction.txt',
    },
    {
        'name': 'add ioc code multiple',
        'description': 'Tests if the add-ioc-code command works correctly with many codes.',
        'protocol': 'addioccode.txt',
    },
    {
        'name': 'add ioc code special',
        'description': 'Tests if the add-ioc-code command works correctly for special cases.',
        'protocol': 'addioccodespecial.txt',
    },
    {
        'name': 'list ioc codes',
        'description': 'Tests if the list-ioc-codes command works correctly.',
        'protocol': 'listioccodes.txt',
    },
    {
        'name': 'list ioc codes special',
        'description': 'Tests if the list-ioc-codes command works correctly for special cases.',
        'protocol': 'listioccodesspecial.txt',
    },
    {
        'name': 'list ioc codes same year',
        'description': 'Tests if the list-ioc-codes command works correctly when multiple countries have the same determination year.',
        'protocol': 'listioccodesambiguous.txt',
    },
    {
        'name': 'add sports venue',
        'description': 'Tests if the add-sports-venue command works correctly.',
        'protocol': 'addsportsvenue.txt',
    },
    {
        'name': 'add sports venue many',
        'description': 'Tests if the add-sports-venue command works correctly with many venues.',
        'protocol': 'addsportsvenuemany.txt',
    },
    {
        'name': 'add sports venue special',
        'description': 'Tests if the add-sports-venue command works correctly for special cases.',
        'protocol': 'addsportsvenuespecial.txt',
    },
    {
        'name': 'list sports venues',
        'description': 'Tests if the list-sports-venues command works correctly.',
        'protocol': 'listsportsvenues.txt',
    },
    {
        'name': 'list sports venues special',
        'description': 'Tests if the list-sports-venues command works correctly for special cases.',
        'protocol': 'listsportsvenuesspecial.txt',
    },
    {
        'name': 'list sports venues same seat count',
        'description': 'Tests if the list-sports-venues command works correctly with multiple venues with same seat count.',
        'protocol': 'listsportsvenuesambiguous.txt',
    },
    {
        'name': 'add olympic sport mutliple disciplines per sport',
        'description': 'Tests if the add-olympic-sport command works correctly with more than one sport and discipline.',
        'protocol': 'addolympicsport.txt',
    },
    {
        'name': 'add olympic sport special',
        'description': 'Tests if the add-olympic-sport command works correctly for special cases.',
        'protocol': 'addolympicsportspecial.txt',
    },
    {
        'name': 'list olympic sports',
        'description': 'Tests if the list-olympic-sports command works correctly.',
        'protocol': 'listolympicsports.txt',
    },
    {
        'name': 'list olympic sports special',
        'description': 'Tests if the list-olympic-sports command works correctly for special cases.',
        'protocol': 'listolympicsportsspecial.txt',
    },
    {
        'name': 'list olympic sports multiple disciplines per sport',
        'description': 'Tests if the list-olympic-sports command works correctly with multiple disciplines per sport.',
        'protocol': 'listolympicsportsambiguous.txt',
    },
    {
        'name': 'add athlete multiple',
        'description': 'Tests if the add-athlete command works correctly for multiple athletes from different countries and sports.',
        'protocol': 'addathlete.txt',
    },
    {
        'name': 'add athlete special',
        'description': 'Tests if the add-athlete command works correctly for sepcial cases.',
        'protocol': 'addathletespecial.txt',
    },
    {
        'name': 'add athlete one athlete multiple disciplines',
        'description': 'Tests if the add-athlete command works correctly for one athlete that participates in multiple disciplines.',
        'protocol': 'addathleteoneinmany.txt',
    },
    {
        'name': 'add athlete same name',
        'description': 'Tests if the add-athlete command works correctly for same names but different ID.',
        'protocol': 'addathletesamename.txt',
    },
    {
        'name': 'add competition / summary athlete same athlete different year',
        'description': 'Tests if the add-competition and summary-athletes commands work correctly for medals of the same athelete in different years.',
        'protocol': 'addcompsumathletesameguydiffyear.txt',
    },
    {
        'name': 'add competition / summary athletes multiple athletes in multiple years',
        'description': 'Tests if the add-competition and summary-athletes commands work correctly for multiple athletes participating in multiple years.',
        'protocol': 'addcompsumathletemultiple.txt',
    },
    {
        'name': 'add competition / summary athletes one athlete multiple disciplines',
        'description': 'Tests if the add-competition and summary-athletes commands work correctly for on athlete that participates in multiple disciplines.',
        'protocol': 'addcompsumathleteoneinmany.txt',
    },
    {
        'name': 'add competition / summary athletes multiple times same medal in discipline in one year',
        'description': 'Tests if the add-competition and summary-athletes commands work correctly for multiple athletes winning the same medal in one discipline in one year.',
        'protocol': 'addcompsumathletesamemedal.txt',
    },
    {
        'name': 'summary athletes same medal scores',
        'description': 'Tests if the summary-athletes command works correctly for the same medal score on different athletes.',
        'protocol': 'addcompsumathleteambiguous.txt',
    },
    {
        'name': 'summary athletes empty',
        'description': 'Tests if the summary-athletes command works correctly when it should be empty.',
        'protocol': 'addcompsumathleteempty.txt',
    },
    {
        'name': 'olympic medal table empty',
        'description': 'Tests if the olympic-medal-table command works correctly when it should be empty.',
        'protocol': 'omtempty.txt',
    },
    {
        'name': 'olympic medal table many competitions',
        'description': 'Tests if the olympic-medal-table command works correctly with many competitions ran in the same year.',
        'protocol': 'omtsameyear.txt',
    },
    {
        'name': 'olympic medal table many competitions many years and multi participation',
        'description': 'Tests if the olympic-medal-table command works correctly with many competitions ran in many years and athletes that compete in many disciplines.',
        'protocol': 'omtmultiple.txt',
    },
    {
        'name': 'olympic medal table ambiguous scorings',
        'description': 'Tests if the olympic-medal-table command works correctly with ambiguous scorings in gold, silver, bronze and overall medals.',
        'protocol': 'omtambiguous.txt',
    },
    {
        'name': 'reset',
        'description': 'Tests if the reset command works correctly.',
        'protocol': 'reset.txt',
    },
    {
        'name': 'reset admins aren\'t deleted',
        'description': 'Tests if the reset command works correctly and doesn\'t remove admins.',
        'protocol': 'resetnoremoval.txt',
    }
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/functional/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if the data system in general works correctly with correct input.")
sys.exit(0 if success else 1)
