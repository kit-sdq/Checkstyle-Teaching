#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import resolution

success =  resolution.run("JAVA", "PROGRAM", sys.argv, "secret.zip")
sys.exit(0 if success else 1)
