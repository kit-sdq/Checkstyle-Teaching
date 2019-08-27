#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import javalyzer

filenames = [
    ['reject', "*.class", "Please don't upload compiled *.class-files. Submit only readable Java source code (*.java)."],
    ['accept', "*"]
]

packages = [
    ['accept', "*"]
]

imports = [
    ['accept', "java.lang"],
    ['accept', "java.lang.*"],
    ['accept', "java.io"],
    ['accept', "java.io.*"],
    ['reject', "*"]
]

classes = [
    ['require', "NaturalNumberTuple", "Please don't change the name of the given class!"],
    ['reject', "*"]
]

methods = [
    ['require', "NaturalNumberTuple.min"],
    ['require', "NaturalNumberTuple.max"],
    ['require', "NaturalNumberTuple.insert"],
    ['require', "NaturalNumberTuple.remove"],
    ['require', "NaturalNumberTuple.indexOf"],
    ['require', "NaturalNumberTuple.countNumbers"],
    ['require', "NaturalNumberTuple.swap"],
    ['require', "NaturalNumberTuple.toSet"],
    ['require', "NaturalNumberTuple.equals"],
    ['require', "NaturalNumberTuple.print"],
    ['accept', "*"]
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
