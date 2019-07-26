#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import javalyzer

filenames = [
             ["reject", "Terminal.java", "Please don't upload the Terminal class."],
             ["reject", "*.class", "Please don't upload compiled *.class-files. They are of no use for us. Do only submit readable Java source code (*.java)."],
             
             ["accept", "*"]
             ]

packages = [
            ["reject", "edu.kit.informatik._intern"],
            ["reject", "edu.kit.informatik._intern.*"],
            
            ["accept", "*"]
            ]

imports = [
           ["reject", "edu.kit.informatik._intern"],
           ["reject", "edu.kit.informatik._intern.*"],
           ['accept', "java.lang"],
           ['accept', "java.io"],
           ['accept', "java.util"],
           ['accept', "java.lang.*"],
           ['accept', "java.io.*"],
           ['accept', "java.util.*"],
           ['reject', "java.math"],
           ['reject', "java.math.*"],
           ['accept', 'edu.kit.informatik'],
           ['accept', 'edu.kit.informatik.*'],
           ['reject', 'java.beans.*'],
           ['reject', 'java.math.*'],
           ['reject', 'java.net.*'],
           ['reject', 'java.nio.*'],
           ['reject', 'java.rmi.*'],
           ['reject', 'java.security.*'],
           ['reject', 'java.sql.*'],
           ['reject', 'java.text.*'],
           ['reject', 'javax.*'],
           ['reject', 'org.omg.*'],
           ['reject', 'org.w3c.*'],
           ['reject', 'org.xml.*'],
           ['accept', "*"] # accept student-defined packages
           ]

classes = [
           ["reject", "*.Terminal", "Please don't upload the Terminal class."],
           ["reject", "Terminal", "Please don't upload the Terminal class."],
           ["accept", "*"]
           ]

methods = [
           ["accept", "*"]
           ]

success = javalyzer.run(
                        sys.argv[1:],
                        filenames = filenames,
                        packages  = packages,
                        imports   = imports,
                        classes   = classes,
                        methods   = methods
                        )

sys.exit(0 if success else 1)

