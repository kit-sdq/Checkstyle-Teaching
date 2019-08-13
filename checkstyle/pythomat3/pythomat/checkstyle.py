import os
import re
import subprocess

from . import output
from . import ressources

from .checker import Checker
from .exc import ConfigurationError
from .severity import Severity
from .test import Test
from .which import which


# turn camel case into whitespace separated words
decamelcase_re = re.compile(r'((?<=[a-z])[A-Z]|(?<!\A)[A-Z](?=[a-z]))')
decamelcase_sub = r' \1'

# combine checks into groups if ...
# - they are related, and
# - they should have a shared error and warning limit.
# the latter is usually the case whenever the checks are of similar or the same
# severity.
grouping = {
    "Generic Whitespace Check" : "Whitespace Checks",
    "No Whitespace After Check" : "Whitespace Checks",
    "No Whitespace Before" : "Whitespace Checks",
    "Whitespace After Check" : "Whitespace Checks",
    "Whitespace Before Check" : "Whitespace Checks",
    "Whitespace Around Check" : "Whitespace Checks",
    "Empty For Initializer Pad Check" : "Whitespace Checks",
    "Empty For Iterator Pad Check" : "Whitespace Checks",
    "Method Param Pad Check" : "Whitespace Checks",
    "Operator Wrap Check" : "Whitespace Checks",
    "Paren Pad Check" : "Whitespace Checks",
    "Typecast Paren Pad Check" : "Whitespace Checks",
    "File Tab Character Check" : "Whitespace Checks",
    "Javadoc Package Check" : "Javadoc Checks",
    "Javadoc Type Check" : "Javadoc Checks",
    "Javadoc Method Check" : "Javadoc Checks",
    "Javadoc Variable Check" : "Javadoc Checks",
    "Javadoc Style Check" : "Javadoc Checks",
    "Member Name Check" : "Name Checks",
    "Constant Name Check" : "Name Checks",
    "Parameter Name Check" : "Name Checks",
    "Line Length Check" : "Size Checks",
    "File Length Check" : "Size Checks",
    "Method Length Check" : "Size Checks",
    "Parameter Number Check" : "Size Checks",
    "Method Count Check" : "Size Checks",
    "Missing Ctor Check" : "Missing Constructor Check"
}


# we tolerate a certain amount of violations of checkstyle rules for some
# groups of checks.
# a count above error limit will be reported as an error, a count above
# warning limit but below error limit will be counted as a warning and
# anything below warning limit is accepted without complaints.
# the default is 0 for both
error_limits = {
    "Whitespace Checks" : 20,
    "Javadoc Checks" : 3,
    "Parameter Assignment Check" : 3,
    "Line Length Check" : 3,
    "Unused Imports Check" : 3,
    "Spelling Check" : 255,
    "Cyclomatic Complexity Check" : 2,
    "Control Flow Nesting Depth Check" : 2,
    "Literals Without Constant Check" : 255,
    "Strict Duplicate Code Check" : 255
}


warning_limits = {
    "Whitespace Check" : 10,
    "Spelling Check" : 20
}


description = {
    "Spelling Check" : "Spelling is actually only of minor importance here. What we want to see is correct camel casing and no excessive use of abbreviations.",
    "Cyclomatic Complexity Check" : "This test is considered failed by the script if more than two methods have a cyclomatic complexity above 15. This does not take the actual value into account, though. A CC of 60 is obviously more of an issue than a CC of 16."
}


def group_for_module(module):
    """Convert a checkstyle module name into a group name"""
    # AFAIK there are no name clashes in checkstyle modules, so we don't
    # need to fully qualified name here.
    rel_module = module.split(".")[-1]

    # Furthermore we split up the camel cased checkstyle identifier.
    words = decamelcase_re.sub(decamelcase_sub, rel_module)

    # And then we finally map multiple modules into a single group
    if words in grouping:
        words = grouping[words]

    return words


def checkstyle(checker, filenames, config):
    """Check the style of all the provided file names"""

    # identify all submitted java files (need to strip single quotes, because praktomat adds them sometimes
    # TODO: move this unquoting to the Checker class so everyone profits from this
    filenames = [filename[1:-1] if filename.startswith("'") and filename.endswith("'") else filename for filename in filenames]
    java_files = [filename for filename in filenames if filename.lower().endswith(".java")]

    # check if the required jar files do exist
    jars = ["checkstyle-teaching-1.0.jar"]
    for jar in jars:
        if not os.path.isfile(jar):
            raise ConfigurationError("Missing jar archive " + jar)

    # run custom checkstyle code from edu.kit.checkstyle
    command_line = [checker.java_insecure, "-classpath", ":".join(jars), "edu.kit.checkstyle.Main"] + java_files

    # force english checkstyle messages
    env = os.environ.copy()
    env['LC_ALL'] = 'C'

    if not checker.java_insecure or not which(checker.java_insecure):
        raise ConfigurationError("Cannot find java executable")

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
    messages = [line.decode('latin-1') for line in stdout.splitlines() if len(line.strip()) != 0]

    # stop early of we don't have anything to report
    if len(messages) == 0:
        checker.add_result(Severity.SUCCESS)
        return

    # group messages by module
    groups = {}
    for message in messages:
        try:
            module,data = message.split(":", 1)
        except ValueError as e:
            raise ValueError("Cannot split '" + message + "'")

        group = group_for_module(module)
        if not group in groups:
            groups[group] = []
        groups[group].append(data)

    for group,messages in list(groups.items()):
        print_grouped_messages(checker, group, messages)


def print_grouped_messages(checker, group, messages):
    desc = description.get(group)
    url = "https://ilias.studium.kit.edu/goto.php?target=wiki_668714_" + group.replace(" ", "_")
    test = Test(checker, group, url=url, description=desc)
    with test.context():
        test.initialize()

        if len(messages) > error_limits.get(group, 0):
            severity = Severity.FAILURE
            test.severity = severity
            test.message = "Inacceptable number of style violations (" + str(len(messages)) + ")"
        elif len(messages) > warning_limits.get(group, 0):
            severity = Severity.WARNING
            test.severity = severity
            test.message = "Questionable number of style violations (" + str(len(messages)) + ")"
        elif len(messages) > 0:
            severity = Severity.SUCCESS
            test.severity = severity
            test.message = "Acceptable number of style violations (" + str(len(messages)) + ")"
        else:
            severity = Severity.SUCCESS
            test.severity = severity
            test.message = "No style violations"

        # split messages into location and info
        split_messages = [message.split(":",3) for message in messages]

        # use paths relative to the current working directory
        split_messages = [(os.path.relpath(filename).strip()+':'+line + ':' + column,info.strip()) for filename, line, column, info in split_messages]

        # TODO: don't display more than approximately 10-20 messages per
        # category to limit output length.
        output.print_messages(checker.document, (output.Message(severity, info, location) for location, info in split_messages), title="Reported style issues")


def run(filenames, config, limits={}, **kwargs):
    """Run a checkstyle checker and nicely report any issues."""
    # TODO: the argument limits is currently not used but ideally should
    # indicate how many errors are allowed per check, per group of checks and
    # in total.
    checker = Checker(**kwargs)
    with checker.context():
        checker.initialize()

        checkstyle(checker, filenames, config)

    return checker.result


