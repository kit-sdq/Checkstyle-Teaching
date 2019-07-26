#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import pseudo_interactive

success = pseudo_interactive.run(sys.argv[1:], "public.zip")
sys.exit(0 if success else 1)
