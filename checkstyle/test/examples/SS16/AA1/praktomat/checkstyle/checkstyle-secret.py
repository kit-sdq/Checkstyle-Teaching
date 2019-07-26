#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import checkstyle

success = checkstyle.run(
        filenames = sys.argv[1:],
        config = "secret.xml",
        description = "These checks cannot be seen by the students."
)

sys.exit(0 if success else 1)
