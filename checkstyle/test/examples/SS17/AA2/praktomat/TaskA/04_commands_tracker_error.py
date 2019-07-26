#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
		'name'        : 'add missing arguments',
		'description' : 'Tests if the add command prints an error if no arguments are given.',
		'protocol'    : 'add_no_arguments.txt',
	},
	{
		'name'        : 'add type name missing',
		'description' : 'Tests if the add command prints an error if the type name is missing.',
		'protocol'    : 'add_type_missing.txt',
	},
	{
		'name'        : 'add aggregation missing',
		'description' : 'Tests if the add command prints an error if the aggregation is missing.',
		'protocol'    : 'add_aggregation_missing.txt',
	},
	{
		'name'        : 'add type name uppercase',
		'description' : 'Tests if the add command prints an error if the type name contains uppercase.',
		'protocol'    : 'add_type_uppercase.txt',
	},
	{
		'name'        : 'add type name invalid symbols',
		'description' : 'Tests if the add command prints an error if the type name contains invalid symbols.',
		'protocol'    : 'add_type_invalid.txt',
	},
	{
		'name'        : 'add type name exists',
		'description' : 'Tests if the add command prints an error if the type name already exists.',
		'protocol'    : 'add_type_exists.txt',
	},
	{
		'name'        : 'add aggregation unknown',
		'description' : 'Tests if the add command prints an error if the aggregation is unknown.',
		'protocol'    : 'add_aggregation_unknown.txt',
	},
	{
		'name'        : 'goal missing arguments',
		'description' : 'Tests if the goal command prints an error if no arguments are given.',
		'protocol'    : 'goal_no_arguments.txt',
	},
	{
		'name'        : 'goal missing type',
		'description' : 'Tests if the goal command prints an error if no type is given.',
		'protocol'    : 'goal_no_type.txt',
	},
	{
		'name'        : 'goal missing description',
		'description' : 'Tests if the goal command prints an error if no goal description is given.',
		'protocol'    : 'goal_no_description.txt',
	},
	{
		'name'        : 'goal type unknown',
		'description' : 'Tests if the goal command prints an error if the type is unknown.',
		'protocol'    : 'goal_type_unknown.txt',
	},
	{
		'name'        : 'goal description unknown',
		'description' : 'Tests if the goal command prints an error if the goal description is unknown.',
		'protocol'    : 'goal_description_unknown.txt',
	},
	{
		'name'        : 'goal description less-than value missing',
		'description' : 'Tests if the goal command prints an error if the less-than goal description is missing the value.',
		'protocol'    : 'goal_description_less_than_value_missing.txt',
	},
	{
		'name'        : 'goal description less-than value not a number',
		'description' : 'Tests if the goal command prints an error if the less-than goal description has a value that is not a number.',
		'protocol'    : 'goal_description_less_than_value_nan.txt',
	},
	{
		'name'        : 'goal description greater-than value missing',
		'description' : 'Tests if the goal command prints an error if the greater-than goal description is missing the value.',
		'protocol'    : 'goal_description_greater_than_value_missing.txt',
	},
	{
		'name'        : 'goal description greater-than value not a number',
		'description' : 'Tests if the goal command prints an error if the greater-than goal description has a value that is not a number.',
		'protocol'    : 'goal_description_greater_than_value_nan.txt',
	},
	{
		'name'        : 'goal description between value missing',
		'description' : 'Tests if the goal command prints an error if the between goal description is missing a value.',
		'protocol'    : 'goal_description_between_value_missing.txt',
	},
	{
		'name'        : 'goal description between value not a number',
		'description' : 'Tests if the goal command prints an error if the between goal description has a value that is not a number.',
		'protocol'    : 'goal_description_between_value_nan.txt',
	},
	{
		'name'        : 'remove-goal missing arguments',
		'description' : 'Tests if the remove-goal command prints an error if no argument is given.',
		'protocol'    : 'remove_goal_no_arguments.txt',
	},
	{
		'name'        : 'remove-goal type unknown',
		'description' : 'Tests if the remove-goal command prints an error if the type is unknown.',
		'protocol'    : 'remove_goal_type_unknown.txt',
	},
	{
		'name'        : 'remove-goal no goal set',
		'description' : 'Tests if the remove-goal command prints an error if there is no goal set for the type.',
		'protocol'    : 'remove_goal_no_goal_set.txt',
	},
	{
		'name'        : 'goals argument given',
		'description' : 'Tests if the goals command prints an error if there are arguments given.',
		'protocol'    : 'goals_arguments.txt',
	},
	{
		'name'        : 'record missing arguments',
		'description' : 'Tests if the record command prints an error if no arguments are given.',
		'protocol'    : 'record_no_arguments.txt',
	},
	{
		'name'        : 'record type missing',
		'description' : 'Tests if the record command prints an error if the type is missing.',
		'protocol'    : 'record_type_missing.txt',
	},
	{
		'name'        : 'record value missing',
		'description' : 'Tests if the record command prints an error if the value is missing.',
		'protocol'    : 'record_value_missing.txt',
	},
	{
		'name'        : 'record type unknown',
		'description' : 'Tests if the record command prints an error if the type is unknown.',
		'protocol'    : 'record_type_unknown.txt',
	},
	{
		'name'        : 'record value not a number',
		'description' : 'Tests if the record command prints an error if the type is unknown.',
		'protocol'    : 'record_value_nan.txt',
	},
	{
		'name'        : 'state argument given',
		'description' : 'Tests if the state command prints an error if there are arguments given.',
		'protocol'    : 'state_arguments.txt',
	},
	{
		'name'        : 'progress argument given',
		'description' : 'Tests if the progress command prints an error if there are arguments given.',
		'protocol'    : 'progress_arguments.txt',
	},
	{
		'name'        : 'top missing arguments',
		'description' : 'Tests if the top command prints an error if no arguments are given.',
		'protocol'    : 'top_no_arguments.txt',
	},
	{
		'name'        : 'top missing number',
		'description' : 'Tests if the top command prints an error if the number of days is missing.',
		'protocol'    : 'top_no_number.txt',
	},
	{
		'name'        : 'top missing type',
		'description' : 'Tests if the top command prints an error if the type is missing.',
		'protocol'    : 'top_no_type.txt',
	},
	{
		'name'        : 'top not a number',
		'description' : 'Tests if the top command prints an error if the number of days is not a number.',
		'protocol'    : 'top_nan.txt',
	},
	{
		'name'        : 'top number too small',
		'description' : 'Tests if the top command prints an error if the number of days is less than 1.',
		'protocol'    : 'top_number_too_small.txt',
	},
	{
		'name'        : 'goals-week missing arguments',
		'description' : 'Tests if the goals-week command prints an error if no argument is given.',
		'protocol'    : 'goals_week_no_arguments.txt',
	},
	{
		'name'        : 'goals-week not a number',
		'description' : 'Tests if the goals-week command prints an error if the week number is not a number.',
		'protocol'    : 'goals_week_nan.txt',
	},
	{
		'name'        : 'goals-week number too small',
		'description' : 'Tests if the goals-week command prints an error if the week number is less than 1.',
		'protocol'    : 'goals_week_number_too_small.txt',
	},
	{
		'name'        : 'next-day argument given',
		'description' : 'Tests if the next-day command prints an error if there are arguments given.',
		'protocol'    : 'next_day_arguments.txt',
	},
	{
		'name'        : 'set-day missing arguments',
		'description' : 'Tests if the set-day command prints an error if no argument is given.',
		'protocol'    : 'set_day_no_arguments.txt',
	},
	{
		'name'        : 'set-day day not a number',
		'description' : 'Tests if the set-day nan.txt',
		'protocol'    : 'set_day_nan.txt',
	},
	{
		'name'        : 'set-day day too small',
		'description' : 'Tests if the set-day command prints an error if the day is less than 0.',
		'protocol'    : 'set_day_too_small.txt',
	},
	{
		'name'        : 'quit argument given',
		'description' : 'Tests if the quit command prints an error if there are arguments given.',
		'protocol'    : 'quit_arguments.txt',
	},
]

for test in tests:
	test['protocol'] = 'copy/protocols/commands_tracker_error/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
						  description="These test the error handling of the tracker commands.")
sys.exit(0 if success else 1)
