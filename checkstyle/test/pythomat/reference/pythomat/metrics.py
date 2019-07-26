import os
import re
import subprocess

import output
import ressources

from checker import Checker
from exc import ConfigurationError
from test import Test

# turn camel case into whitespace separated words
decamelcase_re = re.compile(r'((?<=[a-z])[A-Z]|(?<!\A)[A-Z](?=[a-z]))')
decamelcase_sub = r' \1'

def decamelcase(string):
    return decamelcase_re.sub(decamelcase_sub, string)


def metrics(checker, filenames, config):
    """Check the style of all the provided file names"""

    # identify all submitted java files
    java_files = [filename for filename in filenames if filename.lower().endswith(".java")]

    # check if the required jar files do exist
    jars = ["checkstyle-teaching-1.0.jar"]
    for jar in jars:
        if not os.path.isfile(jar):
            raise ConfigurationError("Missing jar archive " + jar)

    # run custom checkstyle code from edu.kit.checkstyle
    command_line = [checker.java, "-classpath", ":".join(jars), "edu.kit.checkstyle.Main"] + java_files

    # force english checkstyle messages
    env = os.environ.copy()
    env['LC_ALL'] = 'C'

    process = subprocess.Popen(command_line, env=env, shell=False, stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE)

    stdin = ressources.read(os.path.join("checkstyle", config))
    stdout, stderr = process.communicate(stdin)
    returncode = process.returncode

    # some error occured
    if returncode != 0 or len(stderr.strip()) != 0:
        for line in stdout.splitlines():
            with checker.document.div():
                checker.document.cdata(line)
        for line in stderr.splitlines():
            with checker.document.div():
                checker.document.cdata(line)
        raise ConfigurationError("Return code of checkstyle was " + str(returncode))

    # split up stdout into non-empty lines
    lines = [line for line in stdout.splitlines() if len(line.strip()) != 0]

    groups = {}
    for line in lines:
        module,data = line.split(":", 1)
        group = module.split(".")[-1]
        if not group in groups:
            groups[group] = []
        groups[group].append(data)

    for group,metrics in groups.items():
        values = []
        min_value = None
        min_loc = None
        max_value = None
        max_loc = None
        for metric in metrics:
            filename,line,column,keyword,metric,value = metric.split(':')
            try:
                value = float(value)
            except:
                raise ValueError("Non-numeric metric")

            if keyword.strip() != "metric":
                raise ConfigurationError("Not a metric: " + keyword)

            filename = os.path.relpath(filename)
            if not min_value or value < min_value:
                min_value = value
                min_loc = filename + ':' + line + ':' + column

            if not max_value or value > max_value:
                max_value= value
                max_loc = filename + ':' + line + ':' + column

            values.append(value)

        mean = sum(values)/len(values) if values else 0.0
        mean = "{0:.2f}".format(mean)

        test = Test(checker, " ".join(decamelcase(group).split(" ")[:-1]))
        with test.context():
            test.initialize()

            with checker.document.div():
                checker.document.cdata("Minimum: "  + str(min_value) + " @ " + min_loc)
            with checker.document.div():
                checker.document.cdata("Maximum: "  + str(max_value) + " @ " + max_loc)
            with checker.document.div():
                checker.document.cdata("Mean: "  + str(mean))


def run(filenames, config, limits={}, **kwargs):
    """Run a checkstyle checker and nicely report any issues."""
    # TODO: the argument limits is currently not used but ideally should
    # indicate how many errors are allowed per check, per group of checks and
    # in total.
    checker = Checker(**kwargs)
    with checker.context():
        checker.initialize()

        output.print_messages(checker.document, metrics(checker, filenames, config))

    return checker.result


