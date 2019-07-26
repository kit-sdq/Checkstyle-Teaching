#!/usr/bin/env python

import sys
sys.path.append("../..")

from fakeomat import simulator


# run the praktomat simulator
simulator.run(

# submission:
#   path to the submission directory or zip file
  submission="submission",

# checkers:
#   list of checkers to be executed. checkers can be:
#   - *.java -> install according to package
#   - *.py -> execute the script
#   - *.xml -> run as checkstyle file
#   - compiler|compile|javac -> run java compiler on all *.java files
#   - * -> install into the temporary directory
  checkers = [
      "pythomat",
      "script:filenames.py",
      "copy:EnigmaComponent.java",
      "compiler",
      "copy:public.zip",
      "script:public.py"
  ],

# output:
#   where to print output to. available options are 'firefox', 'chromium',
#   'stdout' and '*.html'.
# output = "firefox"
# output = "output.html"
# output = "stdout" # <- default
#  output = "chromium"
  output = "browser"
)
