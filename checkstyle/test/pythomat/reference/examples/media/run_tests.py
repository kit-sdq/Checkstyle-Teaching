#!/usr/bin/env python

import sys
sys.path.append("../..")

from fakeomat import simulator

simulator.run(
  submission="student.zip",
  checkers = [
      "copy:MyTerminal.java",
      "compiler",
      "pythomat",
      "copy:tests.zip",
      "script:test.py"
  ],
  output = "browser"
)
