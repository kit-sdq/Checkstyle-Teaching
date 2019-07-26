#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'list-flights after public',
        'description': 'Tests if the list-flights command outputs correctly after the public test.',
        'protocol': 'lfpublic.txt',
    },
    {
        'name': 'list-flights empty',
        'description': 'Tests if the list-flights command outputs correctly if there are no flights.',
        'protocol': 'lfnoflights.txt',
    },
    {
        'name': 'add multiple flights different currencies',
        'description': 'Tests if adding multiple flights with different currencies works correctly.',
        'protocol': 'addmultiple.txt',
    },
    {
        'name': 'remove',
        'description': 'Tests if removing one of multiple flights works correctly.',
        'protocol': 'remove.txt',
    },
    {
        'name': 'readd removed flight',
        'description': 'Tests if readding a removed flight works correctly.',
        'protocol': 'removereadd.txt',
    },
    {
        'name': 'reuse removed flight number',
        'description': 'Tests if reusing a removed flight number works correctly.',
        'protocol': 'removereuse.txt',
    },
    {
        'name': 'add then remove',
        'description': 'Tests if adding and then removing a flight works correctly (so that there are no flights again).',
        'protocol': 'addremove.txt',
    },
    {
        'name': 'add then remove multiple flights',
        'description': 'Tests if adding and then removing flights works correctly (so that there are no flights again).',
        'protocol': 'addremovemultiple.txt',
    },
    {
        'name': 'add special airport names',
        'description': 'Tests if adding a flight with special characters in the airport descriptors works correctly.',
        'protocol': 'addspecial.txt',
    },
    {
        'name': 'list-bookings after public',
        'description': 'Tests if the list-bookings command works correctly.',
        'protocol': 'lbpublic.txt',
    },
    {
        'name': 'list-bookings empty',
        'description': 'Tests if the list-bookings command outputs correctly if there are no bookings.',
        'protocol': 'lbnobookings.txt',
    },
    {
        'name': 'booking one flight multiple times different customers',
        'description': 'Tests if booking a flight multiple times by different customers works correctly.',
        'protocol': 'bookmultiplediff.txt',
    },
    {
        'name': 'booking multiple flights multiple times different customers',
        'description': 'Tests if booking multiple flights multiple times by the different customers works correctly.',
        'protocol': 'bookmultiplemultiple.txt',
    },
    {
        'name': 'removing a flight with bookings',
        'description': 'Tests if removing a flight with bookings removes the bookings as well.',
        'protocol': 'removebookings.txt',
    },
    {
        'name': 'booking after removal of bookings',
        'description': 'Tests if booking after removing a booking sets customer and invoice number correctly.',
        'protocol': 'bookafterremove.txt',
    },
    {
        'name': 'do not remove customers',
        'description': 'Tests if removing bookings doesn\'t remove customers with it.',
        'protocol': 'noremovecustomers.txt',
    },
    {
        'name': 'book special customer names',
        'description': 'Tests if booking a flight with special characters in the names works correctly.',
        'protocol': 'bookspecial.txt',
    },
    {
        'name': 'readd removed flight with bookings',
        'description': 'Tests if readding a removed flight that had bookings works correctly.',
        'protocol': 'removereaddwithbookings.txt',
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/functional/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if the program works correctly with correct input.")
sys.exit(0 if success else 1)
