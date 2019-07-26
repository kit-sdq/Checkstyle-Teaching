import patterns
from checker import Checker


def run(filenames, config):
    """Check if the filenames conform to the rules.

    The list of rules is parsed first to last. The first rule that matches
    decides if a file is accepted or rejected.

    The available rules are:

    * accept <filename pattern>
      An accept immediately accepts the filename.

    * reject <filename pattern> <message>
      A reject rule immediately rejectes the filename.

    * require <filename pattern> <message>
      A require rule does not immediately stop processing further rules for
      this filename, but if, in the end, none of the filenames matched the
      rule, the set of filenames will be rejected.

    * warn <filename pattern> <message>
      A warn rule prints a warning if the rule matches, but unlike a reject
      rule it does not stop processing and the filename might be accepted by a
      later rule.

    * expect <filename pattern> <message>
      An expect rule does not influence acceptance of the filename or the set
      of filenames, but only prints a warning if the rule never matches.

    A <filename pattern> is a unix filename matcher (e.g. using * for matching any sequence of characters).
    A <message> is a string template to be displayed to the user when the rule
    is violated and containing an explanation of why the filename was rejected
    by the script.  In this template ${filename} can be used as a placeholder
    for the input filename and ${pattern} as a placeholder for the pattern.

    """

    defaults = {
        'accept' : 'The file ${name} is accepted',
        'warn' : "The file ${name} is unexpected",
        'reject' : "The file ${name} is unexpected",
        'require' : "The file ${pattern} is expected but missing",
        'expected' : "The file ${pattern} is expected but missing"
    }

    checker = Checker()
    with checker.context():
        checker.initialize()

        filenames = [filename for filename in filenames if filename != "pythomat.zip"]

        patterns.match(checker, "Filename check", filenames, config, defaults)

    return checker.result
