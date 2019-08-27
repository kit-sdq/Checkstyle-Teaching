#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import sys

sys.path.append("pythomat.zip")

from pythomat import analysers
from pythomat import ipo
from pythomat import interactive

tests = [
    {
        'name': 'Rock vs Rock',
        'description': 'Tests rock facing rock is evaluated correctly.',
        'protocol': 'rockrock.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/private/functional/rock.txt', 'r').read()},
    },
    {
        'name': 'Rock vs Spock',
        'description': 'Tests rock facing spock is evaluated correctly.',
        'protocol': 'rockspock.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/private/functional/spock.txt', 'r').read()},
    },
    {
        'name': 'Rock vs Paper',
        'description': 'Tests rock facing paper is evaluated correctly.',
        'protocol': 'rockpaper.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/private/functional/paper.txt', 'r').read()},
    },
    {
        'name': 'Rock vs Lizard',
        'description': 'Tests rock facing lizard is evaluated correctly.',
        'protocol': 'rocklizard.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/private/functional/lizard.txt', 'r').read()},
    },
    {
        'name': 'Spock vs Rock',
        'description': 'Tests spock facing rock is evaluated correctly.',
        'protocol': 'spockrock.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/private/functional/rock.txt', 'r').read()},
    },
    {
        'name': 'Spock vs Spock',
        'description': 'Tests spock facing spock is evaluated correctly.',
        'protocol': 'spockspock.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/private/functional/spock.txt', 'r').read()},
    },
    {
        'name': 'Spock vs Paper',
        'description': 'Tests spock facing paper is evaluated correctly.',
        'protocol': 'spockpaper.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/private/functional/paper.txt', 'r').read()},
    },
    {
        'name': 'Spock vs Lizard',
        'description': 'Tests spock facing lizard is evaluated correctly.',
        'protocol': 'spocklizard.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/private/functional/lizard.txt', 'r').read()},
    },
    {
        'name': 'Spock vs Scissors',
        'description': 'Tests spock facing scissors is evaluated correctly.',
        'protocol': 'spockscissors.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/private/functional/scissors.txt', 'r').read()},
    },
    {
        'name': 'Paper vs Spock',
        'description': 'Tests paper facing spock is evaluated correctly.',
        'protocol': 'paperspock.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/private/functional/spock.txt', 'r').read()},
    },
    {
        'name': 'Paper vs Paper',
        'description': 'Tests paper facing paper is evaluated correctly.',
        'protocol': 'paperpaper.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/private/functional/paper.txt', 'r').read()},
    },
    {
        'name': 'Paper vs Lizard',
        'description': 'Tests paper facing lizard is evaluated correctly.',
        'protocol': 'paperlizard.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/private/functional/lizard.txt', 'r').read()},
    },
    {
        'name': 'Paper vs Scissors',
        'description': 'Tests paper facing scissors is evaluated correctly.',
        'protocol': 'paperscissors.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/private/functional/scissors.txt', 'r').read()},
    },
    {
        'name': 'Lizard vs Rock',
        'description': 'Tests lizard facing rock is evaluated correctly.',
        'protocol': 'lizardrock.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/private/functional/rock.txt', 'r').read()},
    },
    {
        'name': 'Lizard vs Spock',
        'description': 'Tests lizard facing spock is evaluated correctly.',
        'protocol': 'lizardspock.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/private/functional/spock.txt', 'r').read()},
    },
    {
        'name': 'Lizard vs Paper',
        'description': 'Tests lizard facing paper is evaluated correctly.',
        'protocol': 'lizardpaper.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/private/functional/paper.txt', 'r').read()},
    },
    {
        'name': 'Lizard vs Lizard',
        'description': 'Tests lizard facing lizard is evaluated correctly.',
        'protocol': 'lizardlizard.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/private/functional/lizard.txt', 'r').read()},
    },
    {
        'name': 'Lizard vs Scissors',
        'description': 'Tests lizard facing scissors is evaluated correctly.',
        'protocol': 'lizardscissors.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/private/functional/scissors.txt', 'r').read()},
    },
    {
        'name': 'Scissors vs Rock',
        'description': 'Tests scissors facing rock is evaluated correctly.',
        'protocol': 'scissorsrock.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/private/functional/rock.txt', 'r').read()},
    },
    {
        'name': 'Scissors vs Spock',
        'description': 'Tests scissors facing spock is evaluated correctly.',
        'protocol': 'scissorsspock.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/private/functional/spock.txt', 'r').read()},
    },
    {
        'name': 'Scissors vs Lizard',
        'description': 'Tests scissors facing lizard is evaluated correctly.',
        'protocol': 'scissorslizard.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/private/functional/lizard.txt', 'r').read()},
    },
    {
        'name': 'Scissors vs Scissors',
        'description': 'Tests scissors facing scissors is evaluated correctly.',
        'protocol': 'scissorsscissors.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/private/functional/scissors.txt', 'r').read()},
    },
    {
        'name': 'Losing single run',
        'description': 'Tests if losing with one run works correctly.',
        'protocol': 'loseone.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/private/functional/onerun.txt', 'r').read()},
    },
    {
        'name': 'Drawing single run',
        'description': 'Tests if drawing with one run works correctly.',
        'protocol': 'drawone.txt',
        'arguments': 'input.txt 1',
        'files': {"input.txt": open('copy/input/private/functional/onerun.txt', 'r').read()},
    },
    {
        'name': 'Winning three runs',
        'description': 'Tests if winning with three runs works correctly.',
        'protocol': 'winthree.txt',
        'arguments': 'input.txt 3',
        'files': {"input.txt": open('copy/input/private/functional/threeruns.txt', 'r').read()},
    },
    {
        'name': 'Losing three runs',
        'description': 'Tests if losing with three runs works correctly.',
        'protocol': 'losethree.txt',
        'arguments': 'input.txt 3',
        'files': {"input.txt": open('copy/input/private/functional/threeruns.txt', 'r').read()},
    },
    {
        'name': 'Drawing three runs',
        'description': 'Tests if drawing with three runs works correctly.',
        'protocol': 'drawthree.txt',
        'arguments': 'input.txt 3',
        'files': {"input.txt": open('copy/input/private/functional/threeruns.txt', 'r').read()},
    },
    {
        'name': 'Winning five runs',
        'description': 'Tests if winning with five runs works correctly.',
        'protocol': 'winfive.txt',
        'arguments': 'input.txt 5',
        'files': {"input.txt": open('copy/input/private/functional/fiveruns.txt', 'r').read()},
    },
    {
        'name': 'Losing five runs',
        'description': 'Tests if losing with five runs works correctly.',
        'protocol': 'losefive.txt',
        'arguments': 'input.txt 5',
        'files': {"input.txt": open('copy/input/private/functional/fiveruns.txt', 'r').read()},
    },
    {
        'name': 'Drawing five runs',
        'description': 'Tests if drawing with five runs works correctly.',
        'protocol': 'drawfive.txt',
        'arguments': 'input.txt 5',
        'files': {"input.txt": open('copy/input/private/functional/fiveruns.txt', 'r').read()},
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/functional/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if the program works correctly with correct input.")
sys.exit(0 if success else 1)
