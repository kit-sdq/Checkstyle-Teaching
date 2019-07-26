#!/usr/bin/env python

import sys
sys.path.append("../..")

import setup
from fakeomat import simulator

# run the praktomat simulator
simulator.run(
  submission="submission",
  checkers = [
      "pythomat:nqueens.py",
      "compiler",
      "script:public.py",
  ],
  output = "browser"
)
