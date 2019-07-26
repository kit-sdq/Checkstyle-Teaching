#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import interactive

success = interactive.run(sys.argv[1:], "tests.zip")
sys.exit(0 if success else 1)
