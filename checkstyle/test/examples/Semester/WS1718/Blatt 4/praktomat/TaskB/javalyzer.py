#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import javalyzer

filenames = [
    ['reject', "*.class", "Please don't upload compiled *.class-files. They are of no use for us. Do only submit readable Java source code (*.java)."],
    ['accept', "*"]
]

packages = [
    ['require', "edu.kit.informatik"],
    ['require', "edu.kit.informatik.bank"],
    ['require', "edu.kit.informatik.kunde"],
    ['require', "edu.kit.informatik.konto"],
    ['reject', "*"]
]

imports = [
    ['accept', "java.lang"],
    ['accept', "java.lang.*"],
    ['accept', "java.io"],
    ['accept', "java.io.*"],
    ['accept', "edu.kit.informatik.*"],
    ['reject', "*"]
]

classes = [
    ['require', "*edu.kit.informatik.MinimalList*"],
    ['require', "*edu.kit.informatik.bank.Bank"],
    ['require', "*edu.kit.informatik.konto.Account"],
    ['require', "*edu.kit.informatik.konto.BusinessAccount"],
    ['require', "*edu.kit.informatik.konto.PrivateAccount"],
    ['require', "*edu.kit.informatik.kunde.AccountHolder"],
    ['require', "*edu.kit.informatik.kunde.BusinessAccountHolder"],
    ['require', "*edu.kit.informatik.kunde.PrivateAccountHolder"],
    ['accept', "*edu.kit.informatik.*"],
    ['reject', "*.*", "Please upload all classes specified on the assignment sheet in the specified packages!"],
    ['reject', "*", "Please do not use the default package."]
]

methods = [
    ['reject', "*main", "Please don't upload a main method as it destroys the tests."],
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
