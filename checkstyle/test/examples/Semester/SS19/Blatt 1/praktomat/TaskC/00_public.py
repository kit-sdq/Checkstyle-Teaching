#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name'        : 'Public Test - Task C',
        'description' : 'Tests most basic functionality.',
        'arguments'   : "_edu.kit.informatik.TaskC$Public",
    },
]

success = interactive.run(sys.argv[1:], tests, main="_edu.kit.informatik.Main", description='These are public tests. Remember, we have got a lot more than just these.')
sys.exit(0 if success else 1)
