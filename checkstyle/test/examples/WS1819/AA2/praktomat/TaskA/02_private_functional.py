#!/usr/bin/env python
# -*- encoding: utf-8 -*-
import logging
import os

if not(os.environ.get('PYTHOMAT_TESTS_LOG')):
	log_file = os.path.join(os.path.dirname(os.path.abspath(__file__)), "tests.log")
	os.environ['PYTHOMAT_TESTS_LOG'] = log_file
import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'addAssemblyCaseSensitive',
        'description': 'Tests if the given addAssembly interaction works.',
        'protocol': 'addAssemblyCaseSensitive.txt',
    },
	{
        'name': 'addAssemblyIdentical',
        'description': 'Tests if the given addAssembly interaction works.',
        'protocol': 'addAssemblyIdentical.txt',
    },
	{
        'name': 'addAssemblyMultiple',
        'description': 'Tests if the given addAssembly interaction works.',
        'protocol': 'addAssemblyMultiple.txt',
    },
	{
        'name': 'addAssemblySingleAssembly',
        'description': 'Tests if the given addAssembly interaction works.',
        'protocol': 'addAssemblySingleAssembly.txt',
    },
	{
        'name': 'addAssemblyUnbalancedTree',
        'description': 'Tests if the given addAssembly interaction works.',
        'protocol': 'addAssemblyUnbalancedTree.txt',
    },
	{
        'name': 'addAssemblyWrongInputOrdering',
        'description': 'Tests if the given addAssembly interaction works.',
        'protocol': 'addAssemblyWrongInputOrdering.txt',
    },
	{
        'name': 'removeAssemblyComponent',
        'description': 'Tests if the given removeAssembly interaction works.',
        'protocol': 'removeAssemblyComponent.txt',
    },
	{
        'name': 'removeAssemblyRemoveAssembly',
        'description': 'Tests if the given removeAssembly interaction works.',
        'protocol': 'removeAssemblyRemoveAssembly.txt',
    },
	{
        'name': 'removeAssemblyRemoveAssemblyRetaining',
        'description': 'Tests if the given removeAssembly interaction works.',
        'protocol': 'removeAssemblyRemoveAssemblyRetaining.txt',
    },
	{
        'name': 'removeAssemblyRemoveAssemblyRetaining2',
        'description': 'Tests if the given removeAssembly interaction works.',
        'protocol': 'removeAssemblyRemoveAssemblyRetaining2.txt',
    },
	{
        'name': 'removeAssemblyRemoveComponent',
        'description': 'Tests if the given removeAssembly interaction works.',
        'protocol': 'removeAssemblyRemoveComponent.txt',
    },
	{
        'name': 'removeAssemblyMulitpleOccurencies',
        'description': 'Tests if the given removeAssembly interaction works.',
        'protocol': 'removeAssemblyMulitpleOccurencies.txt',
    },
	{
        'name': 'printAssemblyCase',
        'description': 'Tests if the given printAssembly interaction works.',
        'protocol': 'printAssemblyCase.txt',
    },
	{
        'name': 'getAssembliesOnlyComponents',
        'description': 'Tests if the given getAssemblies interaction works.',
        'protocol': 'getAssembliesOnlyComponents.txt',
    },
	{
        'name': 'getAssembliesAlphabeticalSorting',
        'description': 'Tests if the given getAssemblies interaction works.',
        'protocol': 'getAssembliesAlphabeticalSorting.txt',
    },
	{
        'name': 'getAssembliesCrossedPath',
        'description': 'Tests if the given getAssemblies interaction works.',
        'protocol': 'getAssembliesCrossedPath.txt',
    },
	{
        'name': 'getAssembliesMultiPathsOneLevel',
        'description': 'Tests if the given getAssemblies interaction works.',
        'protocol': 'getAssembliesMultiPathsOneLevel.txt',
    },
	{
        'name': 'getAssembliesMultiPathsThreeLevels',
        'description': 'Tests if the given getAssemblies interaction works.',
        'protocol': 'getAssembliesMultiPathsThreeLevels.txt',
    },
	{
        'name': 'getAssembliesMultiPathsTwoLevels',
        'description': 'Tests if the given getAssemblies interaction works.',
        'protocol': 'getAssembliesMultiPathsTwoLevels.txt',
    },
	{
        'name': 'getAssembliesOnePath',
        'description': 'Tests if the given getAssemblies interaction works.',
        'protocol': 'getAssembliesOnePath.txt',
    },
	{
        'name': 'getAssembliesOverflow',
        'description': 'Tests if the given getAssemblies interaction works.',
        'protocol': 'getAssembliesOverflow.txt',
    },
	{
        'name': 'getComponentsAlphabeticalSorting',
        'description': 'Tests if the given getComponents interaction works.',
        'protocol': 'getComponentsAlphabeticalSorting.txt',
    },
	{
        'name': 'getComponentsCrossedPath',
        'description': 'Tests if the given getComponents interaction works.',
        'protocol': 'getComponentsCrossedPath.txt',
    },
	{
        'name': 'getComponentsSorting',
        'description': 'Tests if the given getComponents interaction works.',
        'protocol': 'getComponentsSorting.txt',
    },
	{
        'name': 'getComponentsMultiPathsTwoLevels',
        'description': 'Tests if the given getComponents interaction works.',
        'protocol': 'getComponentsMultiPathsTwoLevels.txt',
    },
	{
        'name': 'getComponentsOnePath',
        'description': 'Tests if the given getComponents interaction works.',
        'protocol': 'getComponentsOnePath.txt',
    },
	{
        'name': 'getComponentsOverflow',
        'description': 'Tests if the given getComponents interaction works.',
        'protocol': 'getComponentsOverflow.txt',
    },
	{
        'name': 'addPartAddAssembly',
        'description': 'Tests if the given addPart interaction works.',
        'protocol': 'addPartAddAssembly.txt',
    },
	{
        'name': 'addPartAddExisting',
        'description': 'Tests if the given addPart interaction works.',
        'protocol': 'addPartAddExisting.txt',
    },
	{
        'name': 'addPartAddExistingOverflow',
        'description': 'Tests if the given addPart interaction works.',
        'protocol': 'addPartAddExistingOverflow.txt',
    },
	{
        'name': 'addPartExistingPart',
        'description': 'Tests if the given addPart interaction works.',
        'protocol': 'addPartExistingPart.txt',
    },
	{
        'name': 'addPartNewPart',
        'description': 'Tests if the given addPart interaction works.',
        'protocol': 'addPartNewPart.txt',
    },
	{
        'name': 'removePartNonZero',
        'description': 'Tests if the given removePart interaction works.',
        'protocol': 'removePartNonZero.txt',
    },
	{
        'name': 'removePartRemovesAssemly',
        'description': 'Tests if the given removePart interaction works.',
        'protocol': 'removePartRemovesAssemly.txt',
    },
	{
        'name': 'removePartMulitpleOccurencies',
        'description': 'Tests if the given removePart interaction works.',
        'protocol': 'removePartMulitpleOccurencies.txt',
    },
	{
        'name': 'removePartZero',
        'description': 'Tests if the given removePart interaction works.',
        'protocol': 'removePartZero.txt',
    }
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/functional/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if the data system in general interaction works correctly with correct input.")
sys.exit(0 if success else 1)
