#!/usr/bin/env python

import sys
sys.path.append("../..")

from fakeomat import simulator

if len(sys.argv) == 1:
    submission = "../../../SS13/assignments/final01/solution/src"
else:
    submission = sys.argv[1]

simulator.run(
  submission=submission,
  checkers = [
      "compiler",
      "pythomat:resolution.py,correction/",
      "script:correction_parser.py",
      "script:correction_algorithm.py",
      "script:correction_longer.py"
  ],
  output = "browser"
)
