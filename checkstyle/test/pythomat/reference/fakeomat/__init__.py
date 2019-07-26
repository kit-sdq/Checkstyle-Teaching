def run(command = 'simulate', checkers = None, submission = None, output = 'browser'):
    # we currently only support python 2.7
    from sys import version_info as python_version
    if python_version[0] != 2 or python_version[1] < 7:
        import sys
        sys.stderr.write("Currently, only Python 2.x is supported, with x >= 7.")
        sys.exit(1)

    from argparse import ArgumentParser
    parser = ArgumentParser(description='Pythomat execution.')
    parser.add_argument('-c', '--command',
            default = command,
            metavar = "{simulate|package}",
            help = 'Whether to simulate praktomat or package pythomat.'
    )
    parser.add_argument('-o', '--output',
            default = output,
            metavar = "{browser|FILE.html|stdout}",
            help = "Where the html output should go"
    )
    parser.add_argument('submission',
            nargs = '?',
            help = "Directory or archive containing the solution"
    )
    arguments = parser.parse_args()

    submission = arguments.submission or submission
    output = arguments.output
    command = arguments.command

    from .simulator import UserInputError
    try:
        # run the different commands
        if command == 'simulate':
            from . import simulator
            simulator.run(checkers, submission, output)
        elif command == 'package':
            from . import packager
            packager.run(checkers, submission, output)

    except UserInputError as e:
        from sys import stderr
        stderr.write("Invalid input:\n" + str(e) + '\n')
        return False
