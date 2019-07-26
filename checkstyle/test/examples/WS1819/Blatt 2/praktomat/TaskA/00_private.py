#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
        'name': "Task check",
        'description': "This tests if the task is correctly solved for grading convenience.",
		'stdout': """^((0;1;6;16;36;13;30;64;132;45;94;192;65;134;272;548;1100;2204;4412;8828;17660;35324;70652;141308;282620;565244;1130492;2260988;4521980;9043964;18087932;36175868;72351740;144703484;289406972)|(4;4;-1;-2;5;-8;11;-14;17;-20;23;-26;29;-32;35;-38;41;-44;47;-50)|(2413)|(0\.793460.*))$"""
    }
]

for test in tests:
	pAnalyserList = [
		analysers.ExceptionAnalyser(analysers.NumberOfLinesAnalyser(4)),
		analysers.ExceptionAnalyser(analysers.MatchLineAnalyser(test['stdout'])),
	]
	
	test['analysers'] = {
        'stdout': analysers.ParallelAnalyser(pAnalyserList),
        'stderr': analysers.ExceptionAnalyser(),
		'exit': analysers.ExpectZeroExitAnalyser()
    }

success = ipo.run(sys.argv[1:], tests, description="This tests if the task is correctly solved for grading convenience.")
sys.exit(0 if success else 1)
