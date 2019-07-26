#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
		'name'        : 'sliding-window missing arguments',
		'description' : 'Tests if the sliding-window command prints an error if no arguments are given.',
		'protocol'    : 'sliding_window_no_arguments.txt',
	},
	{
		'name'        : 'sliding-window missing window size',
		'description' : 'Tests if the sliding-window command prints an error if the window size is missing.',
		'protocol'    : 'sliding_window_no_window_size.txt',
	},
	{
		'name'        : 'sliding-window window size not a number',
		'description' : 'Tests if the sliding-window command prints an error if the window size is not a number.',
		'protocol'    : 'sliding_window_size_nan.txt',
	},
	{
		'name'        : 'sliding-window window size too small',
		'description' : 'Tests if the sliding-window command prints an error if the window size is less than 1.',
		'protocol'    : 'sliding_window_too_small.txt',
	},
	{
		'name'        : 'sliding-window window size too big',
		'description' : 'Tests if the sliding-window command prints an error if the window size is greater than the amount of datapoints.',
		'protocol'    : 'sliding_window_too_big.txt',
	},
	{
		'name'        : 'sliding-window missing data',
		'description' : 'Tests if the sliding-window command prints an error if the data is missing.',
		'protocol'    : 'sliding_window_no_data.txt',
	},
	{
		'name'        : 'sliding-window data not a number',
		'description' : 'Tests if the sliding-window command prints an error if a datapoint is not a number.',
		'protocol'    : 'sliding_window_data_nan.txt',
	},
	{
		'name'        : 'normalize missing arguments',
		'description' : 'Tests if the normalize command prints an error if no arguments are given.',
		'protocol'    : 'normalize_no_arguments.txt',
	},
	{
		'name'        : 'normalize missing lower or upper bound',
		'description' : 'Tests if the normalize command prints an error if the lower or upper bound is missing.',
		'protocol'    : 'normalize_missing_lower_upper.txt',
	},
	{
		'name'        : 'normalize lower bound not a number',
		'description' : 'Tests if the normalize command prints an error if the lower bound is not a number.',
		'protocol'    : 'normalize_lower_nan.txt',
	},
	{
		'name'        : 'normalize upper bound not a number',
		'description' : 'Tests if the normalize command prints an error if the upper bound is not a number.',
		'protocol'    : 'normalize_upper_nan.txt',
	},
	{
		'name'        : 'normalize missing data',
		'description' : 'Tests if the normalize command prints an error if the data is missing.',
		'protocol'    : 'normalize_missing_data.txt',
	},
	{
		'name'        : 'normalize data not a number',
		'description' : 'Tests if the normalize command prints an error if a datapoint is not a number.',
		'protocol'    : 'normalize_data_nan.txt',
	},
	{
		'name'        : 'normalize lower bound greater than upper bound',
		'description' : 'Tests if the normalize command prints an error if the lower bound is greater than the upper bound.',
		'protocol'    : 'normalize_lower_greater_upper.txt',
	},
	{
		'name'        : 'normalize lower bound equal to upper bound',
		'description' : 'Tests if the normalize command prints an error if the lower bound is equal to the upper bound.',
		'protocol'    : 'normalize_lower_equal_upper.txt',
	},
	{
		'name'        : 'peaks missing arguments',
		'description' : 'Tests if the peaks command prints an error if no arguments are given.',
		'protocol'    : 'peaks_no_arguments.txt',
	},
	{
		'name'        : 'peaks missing threshold or amount',
		'description' : 'Tests if the peaks command prints an error if the threshold or amount is missing.',
		'protocol'    : 'peaks_no_threshold_amount.txt',
	},
	{
		'name'        : 'peaks threshold not a number',
		'description' : 'Tests if the peaks command prints an error if the threshold is not a number.',
		'protocol'    : 'peaks_threshold_nan.txt',
	},
	{
		'name'        : 'peaks amount not a number',
		'description' : 'Tests if the peaks command prints an error if the amount is not a number.',
		'protocol'    : 'peaks_amount_nan.txt',
	},
	{
		'name'        : 'peaks amount too small',
		'description' : 'Tests if the peaks command prints an error if the amount is less than 1.',
		'protocol'    : 'peaks_amount_too_small.txt',
	},
	{
		'name'        : 'peaks missing data',
		'description' : 'Tests if the peaks command prints an error if the data is missing.',
		'protocol'    : 'peaks_no_data.txt',
	},
	{
		'name'        : 'peaks data not a number',
		'description' : 'Tests if the peaks command prints an error if a datapoint is not a number.',
		'protocol'    : 'peaks_data_nan.txt',
	},
	{
		'name'        : 'pulse missing arguments',
		'description' : 'Tests if the pulse command prints an error if no argument is given.',
		'protocol'    : 'pulse_no_arguments.txt',
	},
	{
		'name'        : 'pulse data not a number',
		'description' : 'Tests if the pulse command prints an error if a datapoint is not a number.',
		'protocol'    : 'pulse_data_nan.txt',
	},
	
]

for test in tests:
	test['protocol'] = 'copy/protocols/commands_pulse_error/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
						  description="These test the error handling ofthe pulse commands.")
sys.exit(0 if success else 1)
