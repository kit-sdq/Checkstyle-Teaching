#!/usr/bin/env python

import sys
sys.path.append("../..")

from fakeomat import simulator

simulator.run(
  submission="submission",
  checkers = [
      "pythomat:tsp.py",
      "compiler",
      "script:public.py",
  ],
  output = "browser"
)
