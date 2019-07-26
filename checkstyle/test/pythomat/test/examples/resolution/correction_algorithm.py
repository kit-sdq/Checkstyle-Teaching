#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

import resolution

success = resolution.run(sys.argv[1:], "correction/algorithm_stability")
sys.exit(0 if success else 1)
