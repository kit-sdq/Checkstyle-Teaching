#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
		'name'        : 'quit',
		'description' : 'Tests if the quit command works correctly directly after starting.',
		'protocol'    : 'quit.txt',
	},
	{
		'name'        : 'add sum',
		'description' : 'Tests if the add command works correctly for the sum aggregation.',
		'protocol'    : 'add_sum.txt',
	},
	{
		'name'        : 'add avg',
		'description' : 'Tests if the add command works correctly for the average aggregation.',
		'protocol'    : 'add_avg.txt',
	},
	{
		'name'        : 'add last',
		'description' : 'Tests if the add command works correctly for the last aggregation.',
		'protocol'    : 'add_last.txt',
	},
	{
		'name'        : 'add minmax',
		'description' : 'Tests if the add command works correctly for the minmax aggregation.',
		'protocol'    : 'add_minmax.txt',
	},
	{
		'name'        : 'goal less-than integer',
		'description' : 'Tests if the goal command works correctly for creating a less-than goal and an integer value.',
		'protocol'    : 'goal_lessthan_int.txt',
	},
	{
		'name'        : 'goal less-than float',
		'description' : 'Tests if the goal command works correctly for creating a less-than goal and a float value.',
		'protocol'    : 'goal_lessthan_float.txt',
	},
	{
		'name'        : 'goal less-than overwrite',
		'description' : 'Tests if the goal command works correctly for overwriting an existing goal with a less-than goal.',
		'protocol'    : 'goal_lessthan_overwrite.txt',
	},
	{
		'name'        : 'goal greater-than integer',
		'description' : 'Tests if the goal command works correctly for creating a greater-than goal and an integer value.',
		'protocol'    : 'goal_greaterthan_int.txt',
	},
	{
		'name'        : 'goal greater-than float',
		'description' : 'Tests if the goal command works correctly for creating a greater-than goal and a float value.',
		'protocol'    : 'goal_greaterthan_float.txt',
	},
	{
		'name'        : 'goal greater-than overwrite',
		'description' : 'Tests if the goal command works correctly for overwriting an existing goal with a greater-than goal.',
		'protocol'    : 'goal_greaterthan_overwrite.txt',
	},
	{
		'name'        : 'goal between integer',
		'description' : 'Tests if the goal command works correctly for creating a between goal and integer values.',
		'protocol'    : 'goal_between_int.txt',
	},
	{
		'name'        : 'goal between float',
		'description' : 'Tests if the goal command works correctly for creating a between goal and float values.',
		'protocol'    : 'goal_between_float.txt',
	},
	{
		'name'        : 'goal between overwrite',
		'description' : 'Tests if the goal command works correctly for overwriting an existing goal with a between goal.',
		'protocol'    : 'goal_between_overwrite.txt',
	},
	{
		'name'        : 'remove-goal',
		'description' : 'Tests if the remove-goal command works correctly.',
		'protocol'    : 'remove_goal.txt',
	},
	{
		'name'        : 'goals less-than integer',
		'description' : 'Tests if the goals command works correctly for a less-than goal and an integer value.',
		'protocol'    : 'goals_lessthan_int.txt',
	},
	{
		'name'        : 'goals less-than float',
		'description' : 'Tests if the goals command works correctly for a less-than goal and a float value.',
		'protocol'    : 'goals_lessthan_float.txt',
	},
	{
		'name'        : 'goals greater-than integer',
		'description' : 'Tests if the goals command works correctly for a greater-than goal and an integer value.',
		'protocol'    : 'goals_greaterthan_int.txt',
	},
	{
		'name'        : 'goals greater-than float',
		'description' : 'Tests if the goals command works correctly for a greater-than goal and an float value.',
		'protocol'    : 'goals_greaterthan_float.txt',
	},
	{
		'name'        : 'goals between integer',
		'description' : 'Tests if the goals command works correctly for a between goal and integer values.',
		'protocol'    : 'goals_between_int.txt',
	},
	{
		'name'        : 'goals between float',
		'description' : 'Tests if the goals command works correctly for a between goal and float values.',
		'protocol'    : 'goals_between_float.txt',
	},
	{
		'name'        : 'goals multiple',
		'description' : 'Tests if the goals command works correctly with multiple goals present.',
		'protocol'    : 'goals_multiple.txt',
	},
	{
		'name'        : 'goals after overwrite and remove',
		'description' : 'Tests if the goals command works correctly after overwriting and removing goals.',
		'protocol'    : 'goals_overwrite_remove.txt',
	},
	{
		'name'        : 'record sum integer',
		'description' : 'Tests if the record command works correctly for the sum aggregation with integer values.',
		'protocol'    : 'record_sum_int.txt',
	},
	{
		'name'        : 'record sum float',
		'description' : 'Tests if the record command works correctly for the sum aggregation with float values.',
		'protocol'    : 'record_sum_float.txt',
	},
	{
		'name'        : 'record avg integer',
		'description' : 'Tests if the record command works correctly for the average aggregation with integer values.',
		'protocol'    : 'record_avg_int.txt',
	},
	{
		'name'        : 'record avg float',
		'description' : 'Tests if the record command works correctly for the average aggregation with float values.',
		'protocol'    : 'record_avg_float.txt',
	},
	{
		'name'        : 'record last integer',
		'description' : 'Tests if the record command works correctly for the last aggregation with integer values.',
		'protocol'    : 'record_last_int.txt',
	},
	{
		'name'        : 'record last float',
		'description' : 'Tests if the record command works correctly for the last aggregation with float values.',
		'protocol'    : 'record_last_float.txt',
	},
	{
		'name'        : 'record minmax integer',
		'description' : 'Tests if the record command works correctly for the minmax aggregation with integer values.',
		'protocol'    : 'record_minmax_int.txt',
	},
	{
		'name'        : 'record minmax float',
		'description' : 'Tests if the record command works correctly for the minmax aggregation with float values.',
		'protocol'    : 'record_minmax_float.txt',
	},
	{
		'name'        : 'state sum',
		'description' : 'Tests if the state command works correctly for the sum aggregation.',
		'protocol'    : 'state_sum.txt',
	},
	{
		'name'        : 'state avg',
		'description' : 'Tests if the state command works correctly for the average aggregation.',
		'protocol'    : 'state_avg.txt',
	},
	{
		'name'        : 'state last',
		'description' : 'Tests if the state command works correctly for the last aggregation.',
		'protocol'    : 'state_last.txt',
	},
	{
		'name'        : 'state minmax',
		'description' : 'Tests if the state command works correctly for the minmax aggregation.',
		'protocol'    : 'state_minmax.txt',
	},
	{
		'name'        : 'state multiple',
		'description' : 'Tests if the state command works correctly for multiple types.',
		'protocol'    : 'state_multiple.txt',
	},
	{
		'name'        : 'progress less-than single value aggregation',
		'description' : 'Tests if the progress command works correctly for a less-than goal and a single value aggregation.',
		'protocol'    : 'progress_lessthan_single.txt',
	},
	{
		'name'        : 'progress less-than minmax aggregation',
		'description' : 'Tests if the progress command works correctly for a less-than goal and the minmax aggregation.',
		'protocol'    : 'progress_lessthan_minmax.txt',
	},
	{
		'name'        : 'progress greater-than single value aggregation',
		'description' : 'Tests if the progress command works correctly for a greater-than goal and a single value aggregation.',
		'protocol'    : 'progress_greaterthan_single.txt',
	},
	{
		'name'        : 'progress greater-than minmax aggregation',
		'description' : 'Tests if the progress command works correctly for a greater-than goal and the minmax aggregation.',
		'protocol'    : 'progress_greaterthan_minmax.txt',
	},
	{
		'name'        : 'progress between single value aggregation',
		'description' : 'Tests if the progress command works correctly for a between goal and a single value aggregation.',
		'protocol'    : 'progress_between_single.txt',
	},
	{
		'name'        : 'progress between minmax aggregation',
		'description' : 'Tests if the progress command works correctly for a between goal and the minmax aggregation.',
		'protocol'    : 'progress_between_minmax.txt',
	},
	{
		'name'        : 'goals-week no goals',
		'description' : 'Tests if the goals-week command works correctly if no goals are set.',
		'protocol'    : 'goals_week_no_goals.txt',
	},
]

for test in tests:
	test['protocol'] = 'copy/protocols/commands_tracker_onday/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
						  description="These test the tracker commands for correct functionality without changing the day.")
sys.exit(0 if success else 1)
