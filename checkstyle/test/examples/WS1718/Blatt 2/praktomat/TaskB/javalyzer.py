#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import javalyzer

filenames = [
    ['reject', "*.class", "Please don't upload compiled *.class-files. They are of no use for us. Do only submit readable Java source code (*.java)."],
    ['expect', "*Date.java", "Missing Date.java! Please keep in mind that you should use the sample solution of assignment 1 Task D."],
    ['expect', "*Episode.java", "Missing Episode.java! Please keep in mind that you should use the sample solution of assignment 1 Task D."],
    ['expect', "*Genre.java", "Missing Genre.java! Please keep in mind that you should use the sample solution of assignment 1 Task D."],
    ['expect', "*Person.java", "Missing Person.java! Please keep in mind that you should use the sample solution of assignment 1 Task D."],
    ['expect', "*Point.java"],
    ['expect', "*Season.java", "Missing Season.java! Please keep in mind that you should use the sample solution of assignment 1 Task D."],
    ['expect', "*Series.java", "Missing Series.java! Please keep in mind that you should use the sample solution of assignment 1 Task D."],
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
    ['accept', "*"]
]

methods = [
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
