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
    ['reject', "*"]
]

imports = [
    ['accept', "java.lang"],
    ['accept', "java.lang.*"],
    ['accept', "java.io"],
    ['accept', "java.io.*"],
    ['reject', "*"]
]

classes = [
    ['require', "*edu.kit.informatik.Account"],
    ['require', "*edu.kit.informatik.Bank"],
    ['reject', "*.*", "Please only upload the classes edu.kit.informatik.Account and edu.kit.informatik.Bank."],
    ['reject', "*", "Please do not use the default package."]
]

methods = [
    ['require', "*edu.kit.informatik.Account.withdraw"],
    ['require', "*edu.kit.informatik.Account.deposit"],
    ['require', "*edu.kit.informatik.Account.getBalance"],
    ['require', "*edu.kit.informatik.Account.getAccountNumber"],
    ['require', "*edu.kit.informatik.Bank.createAccount"],
    ['require', "*edu.kit.informatik.Bank.removeAccount"],
    ['require', "*edu.kit.informatik.Bank.containsAccount"],
    ['require', "*edu.kit.informatik.Bank.internalBankTransfer"],
    ['require', "*edu.kit.informatik.Bank.length"],
    ['require', "*edu.kit.informatik.Bank.size"],
    ['require', "*edu.kit.informatik.Bank.getAccount"],
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
