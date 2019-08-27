#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import javalyzer

filenames = [
    ['reject', "*.class", "Please don't upload compiled *.class-files.They are of no use for us. Do only submit readable Java source code (*.java)."],
    ['accept', "*"]
]

packages = [
    ['accept', "*"]
]

imports = [
    ['accept', "*"]
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
