#!/usr/bin/env python

import sys
sys.path.append("../..")

from fakeomat import simulator

if len(sys.argv) == 1:
    submission = "submission"
else:
    submission = sys.argv[1]

simulator.run(
  submission=submission,
  checkers = [
      "pythomat:roomba.py,worlds/,nonstop/,nonstop-v2,nonstop-v3",
      "copy:kit/Robot.java",
      "copy:kit/example/World.java",
      "copy:kit/example/MyRobot.java",
      "copy:kit/example/InputFormatException.java",
      "copy:kit/test/LoggingRobot.java",
      "copy:kit/test/LogInterpreter.java",
      "copy:kit/test/RobotTest.java",
      "compiler",
      "script:roomba-public.py",
      "script:roomba-secret.py"
  ],
  output = "browser"
)
