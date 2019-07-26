#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

import roomba

success =  roomba.run(sys.argv[1:], "worlds")
sys.exit(0 if success else 1)
