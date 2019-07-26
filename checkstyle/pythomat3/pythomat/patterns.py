"""The module patterns provides file-globbing-like matching of items."""
import fnmatch
import re
import string

from .exc import ConfigurationError
from .exc import StudentFailure
from .exc import StudentWarning
from .output import Message
from .output import print_messages
from .severity import Severity
from .test import Test


class RuleException(Exception):
    """Class representing exceptions during rule parsing.

    This exception type should be thrown if an error occured during rule
    parsing.
    """
    def __init__(self, rule, message):
        Exception.__init__(self, message)
        self.rule = rule

    def __str__(self):
        return "Rule " + str(self.rule) + ": " + self.message


class Rule(object):
    """A rule for matching short strings, e.g. file names or package names"""
    def __init__(self):
        self.Type = None
        self.Pattern = None
        self.Regexp = None
        self.Template = None
        self.Matched = False

    def __str__(self):
        if self.Type != "accept":
            return self.Type + ":" + self.Pattern + ":" + self.Template.template
        else:
            return self.Type + ":" + self.Pattern

    accept = 0
    warn = 1
    reject = 2
    require = 3
    expect = 4

    length = {
        'accept' : [2, 3],
        'warn' : [2, 3],
        'reject' : [2, 3],
        'require' : [2, 3],
        'expect' : [2, 3]
    }

    defaults = {
        'accept' : "${name} is accepted",
        'warn' : "${name} is unexpected",
        'reject' : "${name} is unexpected",
        'require' : "${pattern} is expected but missing",
        'expect' : "${pattern} is expected but missing"
    }


def parse_rule_config(config, defaults):
    """Check correctness of the rule descriptions and compile the regular expressions."""

    defaults = dict(list(Rule.defaults.items()) + list(defaults.items()))

    for num, line in enumerate(config):
        rule = Rule()

        # line must be of a proper type, ...
        if line[0] not in Rule.length:
            raise RuleException(num, "Not have a valid rule type (" + line[0] + ")")
        
        rule.Type = line[0]

        # .. have the proper number of arguments, ...
        low = Rule.length[line[0]][0]
        high = Rule.length[line[0]][1]
        if not low <= len(line) <= high:
            raise RuleException(num, "Unexpected number of arguments (" + str(len(line)) + " instead of something between " + str(low) + " and " + str(high) + ")")

        # ... needs a working file name matcher, ...
        try:
            regexp = re.compile(fnmatch.translate(line[1]))
        except:
            raise RuleException(num, "Not a valid name matcher (" + line[1] + ")")

        rule.Pattern = line[1]
        rule.Regexp = regexp

        # ... use the template or the default ...
        if len(line) > 2:
            template_string = line[2]
        else:
            template_string = defaults[rule.Type]

        # ... check if the template only contains allowed patterns ...
        for match in re.findall("\${([^{}]*)}", template_string):
            if match == "name":
                if rule.Type == "require" or rule.Type == "expect":
                    raise RuleException(num, "Template pattern ${name} not allowed in " + rule.Type + " rules")
            elif match != "pattern":
                raise RuleException(num, "Not a valid message template pattern " + match)


        # ... finally try to turn it into a template object ...
        try:
            template = string.Template(template_string)
        except:
            raise RuleException(num, "Not a valid message template (" + template_string + ")")

        rule.Template = template

        yield rule
        

def check_rules(names, rules):
    errors = False
    warnings = False

    names =  sorted(set(names))

    # first check if all names match the rules
    for name in names:
        for rule in rules:
            # accepting rules:
            if rule.Type == 'accept' or rule.Type == 'require' or rule.Type == 'expect':
                if rule.Regexp.match(name):
                    rule.Matched = True
                    break

            # warning rule:
            elif rule.Type == 'warn':
                if rule.Regexp.match(name):
                    message = rule.Template.safe_substitute(pattern = rule.Pattern, name = name)
                    yield Message(Severity.WARNING, message, name)
                    warnings = True
                    break

            # rejecting rule:
            elif rule.Type == 'reject':
                if rule.Regexp.match(name):
                    message = rule.Template.safe_substitute(pattern = rule.Pattern, name = name)
                    yield Message(Severity.FAILURE, message, name)
                    errors = True
                    break


    # now check if all required and expected names could be found
    for rule in [rule for rule in rules if rule.Type == 'require' or rule.Type == 'expect']:
        if not rule.Matched:
            if rule.Type == 'require':
                message = rule.Template.safe_substitute(pattern = rule.Pattern)
                yield Message(Severity.FAILURE, message)
                errors = True
            else:
                message = rule.Template.safe_substitute(pattern = rule.Pattern)
                yield Message(Severity.WARNING, message)
                warnings = True

    if errors:
        raise StudentFailure("Not passed")

    if warnings:
        raise StudentWarning("Passed, but there are issues you should fix")


def match(checker, name, items, config, defaults={}, description=None):
    """Check if the names conform to the rules.

    The list of rules is parsed first to last. The first rule that matches
    decides if a name is accepted or rejected.

    The available rules are:

    * accept <name pattern>
      An accept immediately accepts the name.

    * reject <name pattern> <message>
      A reject rule immediately rejectes the name.

    * require <name pattern> <message>
      A require rule does not immediately stop processing further rules for
      this name, but if, in the end, none of the names matched the
      rule, the set of names will be rejected.

    * warn <name pattern> <message>
      A warn rule prints a warning if the rule matches, but unlike a reject
      rule it does not stop processing and the name might be accepted by a
      later rule.

    * expect <name pattern> <message>
      An expect rule does not influence acceptance of the name or the set
      of names, but only prints a warning if the rule never matches.

    A <name pattern> is a unix filename matcher (e.g. using * for matching any sequence of characters).
    A <message> is a string template to be displayed to the user when the rule
    is violated and containing an explanation of why the name was rejected
    by the script.  In this template ${name} can be used as a placeholder
    for the input name and ${pattern} as a placeholder for the pattern.

    """

    testcase = Test(checker, name, description)
    with testcase.context():
        testcase.initialize()

        try:
            rules = list(parse_rule_config(config, defaults))
        except RuleException as e:
            # failed rule parsing is the trainer's fault
            raise ConfigurationError(str(e))

        messages = check_rules(items, rules)
        print_messages(checker.document, messages)
