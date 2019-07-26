#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import checkstyle

success = checkstyle.run(
        filenames = sys.argv[1:],
        config = "optional.xml",
        description = "These checks should be passed for a good grading of the submission."
)

sys.exit(0 if success else 1)
