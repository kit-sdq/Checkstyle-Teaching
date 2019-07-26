#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'place no arguments',
        'description': 'Tests if the place command without arguments is handled correctly.',
        'protocol': 'placenoargs.txt',
        'arguments': 'standard 18 2'

    },
    {
        'name': 'place wrong number of arguments',
        'description': 'Tests if the place command with a wrong number of arguments is handled correctly.',
        'protocol': 'placewrongargsnum.txt',
        'arguments': 'standard 18 2'

    },
    {
        'name': 'place illegal arguments',
        'description': 'Tests if the place command with illegal arguments is handled correctly.',
        'protocol': 'placeillegalargs.txt',
        'arguments': 'standard 18 2'

    },
    {
        'name': 'rowprint no arguments',
        'description': 'Tests if the rowprint command without arguments is handled correctly.',
        'protocol': 'rowprintnoargs.txt',
        'arguments': 'standard 18 2'

    },
    {
        'name': 'rowprint wrong number of arguments',
        'description': 'Tests if the rowprint command with a wrong number of arguments is handled correctly.',
        'protocol': 'rowprintwrongargsnum.txt',
        'arguments': 'standard 18 2'

    },
    {
        'name': 'rowprint illegal arguments',
        'description': 'Tests if the rowprint command with illegal arguments is handled correctly.',
        'protocol': 'rowprintillegalargs.txt',
        'arguments': 'standard 18 2'

    },
    {
        'name': 'colprint no arguments',
        'description': 'Tests if the colprint command without arguments is handled correctly.',
        'protocol': 'colprintnoargs.txt',
        'arguments': 'standard 18 2'

    },
    {
        'name': 'colprint wrong number of arguments',
        'description': 'Tests if the colprint command with a wrong number of arguments is handled correctly.',
        'protocol': 'colprintwrongargsnum.txt',
        'arguments': 'standard 18 2'

    },
    {
        'name': 'colprint illegal arguments',
        'description': 'Tests if the colprint command with illegal arguments is handled correctly.',
        'protocol': 'colprintillegalargs.txt',
        'arguments': 'standard 18 2'

    },
    {
        'name': 'print with arguments',
        'description': 'Tests if the print command with arguments is handled correctly.',
        'protocol': 'printargs.txt',
        'arguments': 'standard 18 2'

    },
    {
        'name': 'state no arguments',
        'description': 'Tests if the state command without arguments is handled correctly.',
        'protocol': 'statenoargs.txt',
        'arguments': 'standard 18 2'

    },
    {
        'name': 'state wrong number of arguments',
        'description': 'Tests if the state command with a wrong number of arguments is handled correctly.',
        'protocol': 'statewrongargsnum.txt',
        'arguments': 'standard 18 2'

    },
    {
        'name': 'state illegal arguments',
        'description': 'Tests if the state command with illegal arguments is handled correctly.',
        'protocol': 'stateillegalargs.txt',
        'arguments': 'standard 18 2'

    },
    {
        'name': 'reset with arguments',
        'description': 'Tests if the reset command with arguments is handled correctly.',
        'protocol': 'resetargs.txt',
        'arguments': 'standard 18 2'

    },
    {
        'name': 'quit with arguments',
        'description': 'Tests if the quit command with arguments is handled correctly.',
        'protocol': 'quitargs.txt',
        'arguments': 'standard 18 2'

    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/errsyntactic/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="Tests if syntactical errors are correctly handled.")
sys.exit(0 if success else 1)
