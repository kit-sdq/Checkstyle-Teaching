#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name'        : 'reverse',
        'description' : 'Tests reverse',
        'arguments'   : "_edu.kit.informatik.TaskC$Rev",
    },
    {
        'name'        : 'isPalindrome',
        'description' : 'Tests isPalindrome',
        'arguments'   : "_edu.kit.informatik.TaskC$Pal",
    },
    {
        'name'        : 'removeCharacter',
        'description' : 'Tests removeCharacter',
        'arguments'   : "_edu.kit.informatik.TaskC$Rem",
    },
    {
        'name'        : 'isAnagram',
        'description' : 'Tests isAnagram',
        'arguments'   : "_edu.kit.informatik.TaskC$Ana",
    },
    {
        'name'        : 'capitalize',
        'description' : 'Tests capitalize',
        'arguments'   : "_edu.kit.informatik.TaskC$Cap",
    },
    {
        'name'        : 'countCharacter',
        'description' : 'Tests countCharacter',
        'arguments'   : "_edu.kit.informatik.TaskC$Cou",
    },
]

success = interactive.run(sys.argv[1:], tests, main="_edu.kit.informatik.Main", description='These are private tests.')
sys.exit(0 if success else 1)
