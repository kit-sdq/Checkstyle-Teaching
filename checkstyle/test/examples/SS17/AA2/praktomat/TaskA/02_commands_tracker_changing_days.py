#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
		'name'        : 'next-day everything fine',
		'description' : 'Tests if the next-day command works correctly if every type has a value.',
		'protocol'    : 'next_day_fine.txt',
	},
	{
		'name'        : 'next-day everything fine with goal reached',
		'description' : 'Tests if the next-day command works correctly if every type has a value, a goal exists and is reached.',
		'protocol'    : 'next_day_fine_reached.txt',
	},
	{
		'name'        : 'next-day everything fine with goal not reached',
		'description' : 'Tests if the next-day command works correctly if every type has a value, a goal exists and is not reached.',
		'protocol'    : 'next_day_fine_not_reached.txt',
	},
	{
		'name'        : 'next-day value missing',
		'description' : 'Tests if the next-day command works correctly if a value is missing.',
		'protocol'    : 'next_day_missing.txt',
	},
	{
		'name'        : 'next-day values multiple missing',
		'description' : 'Tests if the next-day command works correctly if some values are missing.',
		'protocol'    : 'next_day_missing_multiple.txt',
	},
	{
		'name'        : 'set-day everything fine',
		'description' : 'Tests if the set-day command works correctly if every type has a value.',
		'protocol'    : 'set_day_fine.txt',
	},
	{
		'name'        : 'set-day values missing',
		'description' : 'Tests if the set-day command works correctly if some values are missing.',
		'protocol'    : 'set_day_missing.txt',
	},
	{
		'name'        : 'set-day jump multiple days',
		'description' : 'Tests if the set-day command works correctly when skipping multiple days.',
		'protocol'    : 'set_day_multiple.txt',
	},
	{
		'name'        : 'jump and adding types',
		'description' : 'Tests if everything works correctly when jumping around in time while adding types.',
		'protocol'    : 'jump_add_types.txt',
	},
	{
		'name'        : 'jump and record',
		'description' : 'Tests if everything works correctly when jumping around in time while recording values.',
		'protocol'    : 'jump_record.txt',
	},
	{
		'name'        : 'jump and adding goals',
		'description' : 'Tests if everything works correctly when jumping around in time while adding goals.',
		'protocol'    : 'jump_add_goals.txt',
	},
	{
		'name'        : 'top single value aggregation',
		'description' : 'Tests if the top command works correctly with a single value aggregation.',
		'protocol'    : 'top_single_agg.txt',
	},
	{
		'name'        : 'top minmax',
		'description' : 'Tests if the top command works correctly with the minmax aggregation.',
		'protocol'    : 'top_minmax.txt',
	},
	{
		'name'        : 'top empty',
		'description' : 'Tests if the top command works correctly with no values recorded.',
		'protocol'    : 'top_empty.txt',
	},
	{
		'name'        : 'top n greater than days with values',
		'description' : 'Tests if the top command works correctly when n is greater than the amount of days with records.',
		'protocol'    : 'top_n_greater.txt',
	},
	{
		'name'        : 'top with scattered values',
		'description' : 'Tests if the top command works correctly when some days don\'t have values.',
		'protocol'    : 'top_scattered.txt',
	},
	{
		'name'        : 'goals-week one week',
		'description' : 'Tests if the goals-week command works correctly with values in one week.',
		'protocol'    : 'goals_week_inweek.txt',
	},
	{
		'name'        : 'goals-week multiple weeks',
		'description' : 'Tests if the goals-week command works correctly with values in multiple weeks.',
		'protocol'    : 'goals_week_multiple_weeks.txt',
	},
]

for test in tests:
	test['protocol'] = 'copy/protocols/commands_tracker_changing_days/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
						  description="These test the tracker commands for correct functionality while changing days and weeks.")
sys.exit(0 if success else 1)
