#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
		'name'        : 'sliding-window example',
		'description' : 'Tests if the sliding-window command works correctly with the worksheet.',
		'protocol'    : 'sliding_window_example.txt',
	},
	{
		'name'        : 'sliding-window example n 4',
		'description' : 'Tests if the sliding-window command works correctly with the worksheet example and a different window size of 4.',
		'protocol'    : 'sliding_window_example_n4.txt',
	},
	{
		'name'        : 'sliding-window n 1',
		'description' : 'Tests if the sliding-window command works correctly with the worksheet example and a window size of 1.',
		'protocol'    : 'sliding_window_example_n1.txt',
	},
	{
		'name'        : 'sliding-window n max',
		'description' : 'Tests if the sliding-window command works correctly with with the worksheet example and a window size equal to the amount of values.',
		'protocol'    : 'sliding_window_example_nmax.txt',
	},
	{
		'name'        : 'sliding-window negative integers',
		'description' : 'Tests if the sliding-window command works correctly with negative integer values.',
		'protocol'    : 'sliding_window_negative_int.txt',
	},
	{
		'name'        : 'sliding-window negative floats',
		'description' : 'Tests if the sliding-window command works correctly with negative float values.',
		'protocol'    : 'sliding_window_negative_float.txt',
	},
	{
		'name'        : 'sliding-window big example',
		'description' : 'Tests if the sliding-window command works correctly with the big worksheet example.',
		'protocol'    : 'sliding_window_big_example.txt',
	},
	{
		'name'        : 'normalize example',
		'description' : 'Tests if the normalize command works correctly with the worksheet example.',
		'protocol'    : 'normalize_example.txt',
	},
	{
		'name'        : 'normalize example big interval',
		'description' : 'Tests if the normalize command works correctly with the worksheet example and a big interval.',
		'protocol'    : 'normalize_example_big_interval.txt',
	},
	{
		'name'        : 'normalize negative interval',
		'description' : 'Tests if the normalize command works correctly with a negative interval.',
		'protocol'    : 'normalize_negative_interval.txt',
	},
	{
		'name'        : 'normalize negative integers',
		'description' : 'Tests if the normalize command works correctly with negative integers.',
		'protocol'    : 'normalize_negative_int.txt',
	},
	{
		'name'        : 'normalize negative floats',
		'description' : 'Tests if the normalize command works correctly with negative floats.',
		'protocol'    : 'normalize_negative_floats.txt',
	},
	{
		'name'        : 'normalize big example',
		'description' : 'Tests if the normalize command works correctly with the big worksheet example.',
		'protocol'    : 'normalize_big_example.txt',
	},
	{
		'name'        : 'peaks example',
		'description' : 'Tests if the peaks command works correctly with the worksheet example.',
		'protocol'    : 'peaks_example.txt',
	},
	{
		'name'        : 'peaks always over',
		'description' : 'Tests if the peaks command works correctly with values always over the threshold.',
		'protocol'    : 'peaks_always_over.txt',
	},
	{
		'name'        : 'peaks always under',
		'description' : 'Tests if the peaks command works correctly with values always under the threshold.',
		'protocol'    : 'peaks_always_under.txt',
	},
	{
		'name'        : 'peaks negative integers',
		'description' : 'Tests if the peaks command works correctly with negative integers.',
		'protocol'    : 'peaks_negative_integers.txt',
	},
	{
		'name'        : 'peaks negative floats',
		'description' : 'Tests if the peaks command works correctly with negative floats.',
		'protocol'    : 'peaks_negative_floats.txt',
	},
	{
		'name'        : 'peaks negative threshold',
		'description' : 'Tests if the peaks command works correctly with a negative threshold.',
		'protocol'    : 'peaks_negative_threshold.txt',
	},
	{
		'name'        : 'peaks amount too big',
		'description' : 'Tests if the peaks command works correctly if the amount is so big that no peaks are detected.',
		'protocol'    : 'peaks_amount_too_big.txt',
	},
	{
		'name'        : 'peaks big example n 1',
		'description' : 'Tests if the peaks command works correctly with the big worksheet example and an amount of 1.',
		'protocol'    : 'peaks_big_example_n1.txt',
	},
	{
		'name'        : 'peaks big example n 2',
		'description' : 'Tests if the peaks command works correctly with the big worksheet example and an amount of 2.',
		'protocol'    : 'peaks_big_example_n2.txt',
	},
	{
		'name'        : 'pulse big example',
		'description' : 'Tests if the peaks command works correctly with the big worksheet example.',
		'protocol'    : 'pulse_big_example.txt',
	},
]

for test in tests:
	test['protocol'] = 'copy/protocols/commands_pulse/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
						  description="These test the pulse commands for correct functionality.")
sys.exit(0 if success else 1)
