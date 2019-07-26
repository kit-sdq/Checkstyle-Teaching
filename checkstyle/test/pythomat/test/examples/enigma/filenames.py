#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import filenames


rules = [
    ['accept', 'filenames.py'],
    ['reject', '*Steckbrett*', "Bitte programmieren Sie in englisch (${name})."],
    ['reject', '*Umkehr*', "Bitte programmieren Sie in englisch (${name})."],
    ['accept', '*.java'],
    ['reject', '*', "Die Datei '${name}' ist unerwartet. Sind Sie sicher, dass diese zu dieser Abgabe gehört?"]
]

success = filenames.run(sys.argv[1:], rules)
sys.exit(0 if success else 1)
