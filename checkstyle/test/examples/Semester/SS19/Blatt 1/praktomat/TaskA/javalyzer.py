#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import javalyzer

filenames = [
    ['accept', "*Antwort01.txt"],
    ['accept', "*AufgabeA.txt"],
    ['accept', "*javalyzer.py"],
    ['accept', "*checkstyle-teaching-1.0.jar"],
    ['reject', "*", "Please don't upload anything except a .txt file called Antwort01.txt."]
]

packages = [
    ['reject', "*"]
]

imports = [
    ['reject', "*"]
]

classes = [
    ['reject', "*"]
]

methods = [
    ['reject', "*"]
]

success = javalyzer.run(
        sys.argv[1:],
        filenames=filenames,
        packages=packages,
        imports=imports,
        classes=classes,
        methods=methods
)

sys.exit(0 if success else 1)
