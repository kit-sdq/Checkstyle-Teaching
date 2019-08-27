#!/usr/bin/env python

import simplejson
with open("config_mySolution.json") as json_file:
  config = simplejson.load(json_file)

import sys
sys.path.append(config["pythomat"])

import fakeomat
fakeomat.run(
    submission = str(config["sample_solution"]),
    checkers = config["checkers"]
)
