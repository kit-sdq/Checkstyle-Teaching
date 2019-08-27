#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
		'name': "No parameters",
		'description': "Checks for error message when no parameters are given.",
		'arguments': ""
	},
	{
		'name': "Too many parameters",
		'description': "Checks for error message when too many parameters are given.",
		'arguments': "input.txt waitingarea=rr timeslice=1 WTF",
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()}
	},
	{
		'name': "wrong parameter order waitingarea",
		'description': "Checks for error message when waitingarea isn't the second parameter.",
		'arguments': "input.txt timeSlice=1 waitingarea=rr",
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()}
	},
	{
		'name': "Unknown parameter instead of waitingarea",
		'description': "Checks for error message when waitingarea isn't the second parameter.",
		'arguments': "input.txt WTF",
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()}
	},
	{
		'name': "Unknown waitingarea",
		'description': "Checks for error message when waitingarea is unknown.",
		'arguments': "input.txt waitingarea=WTF",
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()}
	},
	{
		'name': "Waitingarea wrong casing",
		'description': "Checks for error message when waitingarea is in wrong casing.",
		'arguments': "input.txt waitingarea=RR",
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()}
	},
	{
		'name': "timeSlice missing",
		'description': "Checks for error message when timeSlice is missing while rr is selected.",
		'arguments': "input.txt waitingarea=rr",
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()}
	},
	{
		'name': "Unknown parameter instead of timeSlice",
		'description': "Checks for error message when timeSlice isn't the third parameter.",
		'arguments': "input.txt waitingarea=rr WTF",
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()}
	},

	{
		'name': "timeSlice NaN",
		'description': "Checks for error message when the timeSlice value is not a number.",
		'arguments': "input.txt waitingarea=rr timeSlice=WTF",
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()}
	},
	{
		'name': "timeSlice < 1",
		'description': "Checks for error message when timeSlice is too small.",
		'arguments': "input.txt waitingarea=rr timeSlice=0",
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()}
	},
	{
		'name': "timeSlice without rr",
		'description': "Checks for error message when timeSlice is given without rr being selected.",
		'arguments': "input.txt waitingarea=fifo timeSlice=1",
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()}
	}
	# {
	# 	'name': "File not found",
	# 	'description': "Checks for error message when file doesn't exist.",
	# 	'arguments': "WTF.txt waitingarea=fifo"
	# },
	# {
	# 	'name': "File empty",
	# 	'description': "Checks for error message when the file is empty.",
	# 	'arguments': "input.txt waitingarea=fifo",
	# 	'files': {"input.txt": ""}
	# },
	# {
	# 	'name': "Line improperly formatted",
	# 	'description': "Checks for error message when a line is formatted wrong.",
	# 	'arguments': "input.txt waitingarea=fifo",
	# 	'files': {"input.txt": "A,simple 0 5"}
	# },
	# {
	# 	'name': "Line empty",
	# 	'description': "Checks for error message when a line is empty.",
	# 	'arguments': "input.txt waitingarea=fifo",
	# 	'files': {"input.txt": """A,simple,0,5
	#
	# 	"""}
	# },
	# {
	# 	'name': "Line name missing",
	# 	'description': "Checks for error message when a line is missing the name.",
	# 	'arguments': "input.txt waitingarea=fifo",
	# 	'files': {"input.txt": ",simple,0,5"}
	# },
	# {
	# 	'name': "Line wrong job type",
	# 	'description': "Checks for error message when the job type is unknown.",
	# 	'arguments': "input.txt waitingarea=fifo",
	# 	'files': {"input.txt": "A,heavy,0,5"}
	# },
	# {
	# 	'name': "Line arrival time NaN",
	# 	'description': "Checks for error message when the arrival time is not a number.",
	# 	'arguments': "input.txt waitingarea=fifo",
	# 	'files': {"input.txt": "A,simple,WTF,5"}
	# },
	# {
	# 	'name': "Line arrival time < 0",
	# 	'description': "Checks for error message when the arrival time is too small.",
	# 	'arguments': "input.txt waitingarea=fifo",
	# 	'files': {"input.txt": "A,simple,-1,5"}
	# },
	# {
	# 	'name': "Line complexity NaN",
	# 	'description': "Checks for error message when the complexity is not a number.",
	# 	'arguments': "input.txt waitingarea=fifo",
	# 	'files': {"input.txt": "A,simple,0,WTF"}
	# },
	# {
	# 	'name': "Line complexity < 1",
	# 	'description': "Checks for error message when the complexity is too small.",
	# 	'arguments': "input.txt waitingarea=fifo",
	# 	'files': {"input.txt": "A,simple,0,0"}
	# }
]

for test in tests:
	test['analysers'] = {
		'stdout': analysers.ExceptionAnalyser(analysers.ErrorAnalyser()),
		'stderr': analysers.ExceptionAnalyser()
	}

success = ipo.run(sys.argv[1:], tests,
				  description="These test correct error handling of command line parameters.")
sys.exit(0 if success else 1)
