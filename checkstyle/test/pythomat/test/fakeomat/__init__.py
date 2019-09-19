import sys
from argparse import ArgumentParser

from .simulator import UserInputError


def run(command='simulate', checkers=None, submission=None, output='browser'):
    if sys.version_info[0] != 3:
        sys.stderr.write('In process of migration, please use Python 3.x.')
        sys.exit(1)

    parser = ArgumentParser(description='Pythomat execution.')
    parser.add_argument('-c', '--command',
                        default=command,
                        metavar="{simulate|package}",
                        help='Whether to simulate praktomat or package pythomat.')
    parser.add_argument('-o', '--output',
                        default=output,
                        metavar="{browser|FILE.html|stdout}",
                        help="Where the html output should go")
    parser.add_argument('submission',
                        nargs='?',
                        help="Directory or archive containing the solution")
    arguments = parser.parse_args()

    command = arguments.command
    output = arguments.output
    submission = arguments.submission or submission

    try:
        if command == 'simulate':
            from . import simulator
            simulator.run(checkers, submission, output)
        elif command == 'package':
            from . import packager
            packager.run(checkers, submission, output)
    except UserInputError as e:
        sys.stderr.write("Invalid input:\n" + str(e) + '\n')
        return False
