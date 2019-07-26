#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

import nqueens

success =  nqueens.run()
sys.exit(0 if success else 1)
