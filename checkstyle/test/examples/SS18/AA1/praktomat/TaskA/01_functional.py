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
        'name': 'Example Interaction with minimal tokens',
        'description': 'Tests if the example interaction from the assignment sheet works correctly with minimal tokens.',
        'protocol': 'public.txt',
        'arguments': 'standard 28',
    },
    {
        'name': 'Example Interaction with uneven number of tokens',
        'description': 'Tests if the example interaction from the assignment sheet works correctly with an uneven number of tokens.',
        'protocol': 'public.txt',
        'arguments': 'standard 29',
    },
    {
        'name': 'token',
        'description': 'Tests if the token command works correctly.',
        'protocol': 'token.txt',
        'arguments': 'standard 32',
    },
    {
        'name': 'win horizontally',
        'description': 'Tests if winning horizontally works correctly.',
        'protocol': 'horizontalwin.txt',
        'arguments': 'standard 32',
    },
    {
        'name': 'win vertically',
        'description': 'Tests if winning vertically works correctly.',
        'protocol': 'verticalwin.txt',
        'arguments': 'standard 32',
    },
    {
        'name': 'win vertically P2',
        'description': 'Tests if winning as player 2 works correctly (exemplary vertically).',
        'protocol': 'win2.txt',
        'arguments': 'standard 32',
    },
    {
        'name': 'win diagonally',
        'description': 'Tests if winning diagonally (standard, not antidiagonal) works correctly.',
        'protocol': 'diagonalwin.txt',
        'arguments': 'standard 32',
    },
    {
        'name': 'draw full board',
        'description': 'Tests if drawing with a full board works correctly.',
        'protocol': 'drawfull.txt',
        'arguments': 'standard 32',
    },
    {
        'name': 'draw no tokens',
        'description': 'Tests if drawing by one player being out of tokens works correctly.',
        'protocol': 'drawnotokens.txt',
        'arguments': 'standard 28',
    },
    {
        'name': 'token after win',
        'description': 'Tests if the token command works correctly after the game is won by a player.',
        'protocol': 'tokenafterwin.txt',
        'arguments': 'standard 32',
    },
    {
        'name': 'token after draw',
        'description': 'Tests if the token command works correctly after the game is drawn.',
        'protocol': 'tokenafterdraw.txt',
        'arguments': 'standard 32',
    },
    {
        'name': 'state before first throwin',
        'description': 'Tests if the state command works correctly before the first token is used.',
        'protocol': 'statebefore.txt',
        'arguments': 'standard 32',
    },
    {
        'name': 'state after draw',
        'description': 'Tests if the state command works correctly after the game is drawn.',
        'protocol': 'stateafterdraw.txt',
        'arguments': 'standard 32',
    },
    {
        'name': 'print before first throwin',
        'description': 'Tests if the print command works correctly on an empty board.',
        'protocol': 'printempty.txt',
        'arguments': 'standard 32',
    },
    {
        'name': 'flip example',
        'description': 'Tests if the flip example from the assignment sheet works correctly.',
        'protocol': 'flipexample.txt',
        'arguments': 'flip 32',
    },
    {
        'name': 'flip doesn\'t decrease tokens and switches the active player',
        'description': 'Tests if the flip command as described in the test title.',
        'protocol': 'flipbehaviour.txt',
        'arguments': 'flip 32',
    },
    {
        'name': 'flip empty board',
        'description': 'Tests if the flip command on an empty board works correctly.',
        'protocol': 'flipempty.txt',
        'arguments': 'flip 32',
    },
    {
        'name': 'flip win not his turn',
        'description': 'Tests if winning by a flip for a player that isn\'t currently active works correctly.',
        'protocol': 'winflipnotactive.txt',
        'arguments': 'flip 32',
    },
    {
        'name': 'flip win both have rows',
        'description': 'Tests if winning by a flip (both players have a winning row, but one has more) works correctly.',
        'protocol': 'flipwin2.txt',
        'arguments': 'flip 32',
    },
    {
        'name': 'flip draw',
        'description': 'Tests if drawing by a flip (both players have a winning row) works correctly.',
        'protocol': 'flipdraw.txt',
        'arguments': 'flip 32',
    },
    {
        'name': 'flip draw 2',
        'description': 'Tests if drawing by a flip (both players have multiple, but the same number of winning rows) works correctly.',
        'protocol': 'flipdraw2.txt',
        'arguments': 'flip 32',
    },
    {
        'name': 'flip draw different lengths',
        'description': 'Tests if drawing by a flip (both players have a winning row, but they have a different length) works correctly.',
        'protocol': 'flipdrawdifflength.txt',
        'arguments': 'flip 32',
    },
    {
        'name': 'flip draw unsynced',
        'description': 'Tests if drawing works correctly after players tokens count got unsynced by a use of flip.',
        'protocol': 'flipdrawunsync.txt',
        'arguments': 'flip 32',
    },
    {
        'name': 'remove dropping down works',
        'description': 'Tests if the remove command correctly drops down the tokens above.',
        'protocol': 'removedrop.txt',
        'arguments': 'remove 32',
    },
    {
        'name': 'remove reinsert',
        'description': 'Tests if the remove command doesn\'t destroy the field and you can still reinsert a removed token.',
        'protocol': 'removereinsert.txt',
        'arguments': 'remove 32',
    },
    {
        'name': 'remove win',
        'description': 'Tests if winning by a remove works correctly.',
        'protocol': 'removewin.txt',
        'arguments': 'remove 32',
    },
    {
        'name': 'remove doesn\'t increase tokens and switches active player',
        'description': 'Tests if the remove command doesn\'t increase the token count of the player and switches to the next player.',
        'protocol': 'removebehaviour.txt',
        'arguments': 'remove 32',
    },
    {
        'name': 'remove win not his turn',
        'description': 'Tests if winning by a remove for a player that isn\'t currently active works correctly.',
        'protocol': 'removewinnotactive.txt',
        'arguments': 'remove 32',
    },
    {
        'name': 'remove draw',
        'description': 'Tests if drawing by a remove (both players have a winning row) works correctly.',
        'protocol': 'removedraw.txt',
        'arguments': 'remove 32',
    },
    {
        'name': 'remove draw 2',
        'description': 'Tests if drawing by a remove (both players have multiple, but the same number of winning rows) works correctly.',
        'protocol': 'removedraw2.txt',
        'arguments': 'remove 32',
    },
    {
        'name': 'remove draw different lengths',
        'description': 'Tests if drawing by a remove (both players have a winning row, but they have a different length) works correctly.',
        'protocol': 'removedrawdifflength.txt',
        'arguments': 'remove 32',
    },
    {
        'name': 'remove draw different number of rows',
        'description': 'Tests if drawing by a remove (both players have a different number of winning rows) works correctly.',
        'protocol': 'removedrawdiffrownum.txt',
        'arguments': 'remove 32',
    },
    {
        'name': 'remove draw unsynced',
        'description': 'Tests if drawing works correctly after players tokens count got unsynced by a use of remove.',
        'protocol': 'removedrawunsync.txt',
        'arguments': 'remove 32',
    },
]

for test in tests:
    test['protocol'] = 'copy/protocols/private/functional/' + test.get('protocol')

success = interactive.run(sys.argv[1:], tests,
                          description="These test if the functionality is given according to the specifications without error handling.")
sys.exit(0 if success else 1)
