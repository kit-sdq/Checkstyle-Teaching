import os
import subprocess
import time

from . import contexts
from .exc import ConfigurationError
from .which import which

def execute_submission(java, submission, environment = {}, arguments = [], stdin = [], timeout = 1, sandbox="sandbox", classpath=[]):
    """Run the student's submission with the argument 'argument' passing the
    array of strings 'stdin' to the new processes standard input stream.
    Return a tuple containing stdout, stderr and the return code."""

    if not os.path.exists(sandbox) or not os.path.isdir(sandbox):
        raise ConfigurationError("Need sandbox to run submission in")

    # split arguments on demand
    if isinstance(arguments, str) or isinstance(arguments, str):
        arguments = arguments.split()

    # check if java is in the PATH
    if not java or not which(java):
        raise ConfigurationError("Could not find java")
    command_line = [java]

    # accept classpaths as strings, too
    if isinstance(classpath, str) or isinstance(classpath, str):
        classpath = classpath.split(':')

    # make sure '..' is in the classpath
    # also make sure '.' is not
    try:
        classpath.remove(".")
    except ValueError:
        pass
    if not ".." in classpath:
        classpath.insert(0, "..")

    command_line += "-classpath", ":".join(classpath)
    
    command_line += [submission] + arguments

    # set locale to english (mostly for local execution)
    env = os.environ.copy()
    env['LC_ALL'] = 'C'
    env.update(environment)

    process = subprocess.Popen(command_line, env=env, shell=False, cwd=sandbox,
            stdin=subprocess.PIPE,
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE)

    with contexts.InternalTimeoutGuard(timeout, process) as timer:
        stdout, stderr = process.communicate('\n'.join(stdin))

    returncode = process.returncode if not timer.timeout_occured else None
    return stdout.splitlines(), stderr.splitlines(), returncode, timer.time
