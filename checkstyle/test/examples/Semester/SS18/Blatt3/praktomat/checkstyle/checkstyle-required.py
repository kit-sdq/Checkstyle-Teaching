#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import checkstyle

success = checkstyle.run(
        filenames = sys.argv[1:],
        config = "required.xml",
        description = "These checks need to be passed for the submission to be acceptable."
)

sys.exit(0 if success else 1)
