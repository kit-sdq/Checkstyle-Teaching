#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys
sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
	{
		'name'        : 'nodes example',
		'description' : 'Tests if the nodes command works correctly with the example from the assignment sheet.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'nodes_example.txt',
	},
	{
		'name'        : 'nodes video',
		'description' : 'Tests if the nodes command works correctly with the video graph.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/video_clean.txt', 'r').read()},
		'protocol'    : 'nodes_video.txt',
	},
	{
		'name'        : 'nodes oo_lo',
		'description' : 'Tests if the nodes command works correctly with the OpenOffice_LibreOffice graph.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/oo_lo.txt', 'r').read()},
		'protocol'    : 'nodes_oo_lo.txt',
	},
	{
		'name'        : 'edges example',
		'description' : 'Tests if the edges command works correctly with the example from the assignment sheet.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'edges_example.txt',
	},
	{
		'name'        : 'edges video',
		'description' : 'Tests if the edges command works correctly with the video graph.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/video_clean.txt', 'r').read()},
		'protocol'    : 'edges_video.txt',
	},
	{
		'name'        : 'edges oo_lo',
		'description' : 'Tests if the edges command works correctly with the OpenOffice_LibreOffice graph.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/oo_lo.txt', 'r').read()},
		'protocol'    : 'edges_oo_lo.txt',
	},
	{
		'name'        : 'recommend S1 example',
		'description' : 'Tests if the S1 strategy works correctly with the example from the assignment sheet.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'recommendS1_example.txt',
	},
	{
		'name'        : 'recommend S1 video',
		'description' : 'Tests if the S1 strategy works correctly with the video graph.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/video_clean.txt', 'r').read()},
		'protocol'    : 'recommendS1_video.txt',
	},
	{
		'name'        : 'recommend S1 oo_lo',
		'description' : 'Tests if the S1 strategy works correctly with the OpenOffice_LibreOffice graph.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/oo_lo.txt', 'r').read()},
		'protocol'    : 'recommendS1_oo_lo.txt',
	},
	{
		'name'        : 'recommend S2 example',
		'description' : 'Tests if the S2 strategy works correctly with the example from the assignment sheet.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'recommendS2_example.txt',
	},
	{
		'name'        : 'recommend S2 video',
		'description' : 'Tests if the S2 strategy works correctly with the video graph.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/video_clean.txt', 'r').read()},
		'protocol'    : 'recommendS2_video.txt',
	},
	{
		'name'        : 'recommend S2 oo_lo',
		'description' : 'Tests if the S2 strategy works correctly with the OpenOffice_LibreOffice graph.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/oo_lo.txt', 'r').read()},
		'protocol'    : 'recommendS2_oo_lo.txt',
	},
	{
		'name'        : 'recommend S3 example',
		'description' : 'Tests if the S3 strategy works correctly with the example from the assignment sheet.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'recommendS3_example.txt',
	},
	{
		'name'        : 'recommend S3 video',
		'description' : 'Tests if the S3 strategy works correctly with the video graph.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/video_clean.txt', 'r').read()},
		'protocol'    : 'recommendS3_video.txt',
	},
	{
		'name'        : 'recommend S3 oo_lo',
		'description' : 'Tests if the S3 strategy works correctly with the OpenOffice_LibreOffice graph.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/oo_lo.txt', 'r').read()},
		'protocol'    : 'recommendS3_oo_lo.txt',
	},
	{
		'name'        : 'recommend random spaces',
		'description' : 'Tests if the recommend command works correctly with additional spaces.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'recommendS1_example_spaces.txt',
	},
	{
		'name'        : 'export example',
		'description' : 'Tests if the export commands works correctly with the example from the assignment sheet.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/example.txt', 'r').read()},
		'protocol'    : 'export_example.txt',
	},
	{
		'name'        : 'export video',
		'description' : 'Tests if the export commands works correctly with the video graph.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/video_clean.txt', 'r').read()},
		'protocol'    : 'export_video.txt',
	},
	{
		'name'        : 'export oo_lo',
		'description' : 'Tests if the export commands works correctly with the OpenOffice_LibreOffice graph.',
		'arguments'   : 'input.txt',
		'files': {"input.txt": open('copy/input/oo_lo.txt', 'r').read()},
		'protocol'    : 'export_oo_lo.txt',
	}

]

for test in tests:
	test['protocol'] = 'copy/protocols/commands/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
						  description="These test all the commands for correct functionality.")
sys.exit(0 if success else 1)
