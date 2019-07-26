"""The severity module contains the class Severity, which enumerates the
different result types, and the class Statistics, which collects the results
for multiple checker or test runs."""

class Severity:
    """Enumeration of the different severity levels checkers, messages, and tests can have."""
    SUCCESS = 0
    INFO = 1
    WARNING = 2
    FAILURE = 3
    CRASH = 4

    @staticmethod
    def string(severity):
        """Return a string representation of a severity."""
        if severity == Severity.SUCCESS:
            return "success"
        elif severity == Severity.INFO:
            return "info"
        elif severity == Severity.WARNING:
            return "warning"
        elif severity == Severity.FAILURE:
            return "failure"
        elif severity == Severity.CRASH:
            return "crash"
        else:
            raise ValueError("Invalid severity")

    @staticmethod
    def plural(severity):
        """Return a string representation the plural of a severity."""
        if severity == Severity.SUCCESS:
            return "successes"
        elif severity == Severity.INFO:
            return "infos"
        elif severity == Severity.WARNING:
            return "warnings"
        elif severity == Severity.FAILURE:
            return "failures"
        elif severity == Severity.CRASH:
            return "crashes"
        else:
            raise ValueError("Invalid severity")

    @staticmethod
    def iterate():
        """Iterate over all Severity values."""
        return  list(range(Severity.SUCCESS, Severity.CRASH+1))


class Statistics(object):
    """Collect statistics about the items of different severities for a test or checker."""
    def __init__(self):
        self.successes = 0
        self.infos = 0
        self.warnings = 0
        self.failures = 0
        self.crashes = 0

    def __str__(self):
        return str(self.successes) + ' ' + str(self.infos) + ' ' + str(self.warnings) + ' ' + str(self.failures) + ' ' + str(self.crashes)

    def __add__(self, other):
        result = Statistics()
        result.successes = self.successes + other.successes
        result.infos = self.infos + other.infos
        result.warnings = self.warnings + other.warnings
        result.failures = self.failures + other.failures
        result.crashes = self.crashes + other.crashes
        return result

    def count(self, severity):
        """Return the number of items of a particular severity."""
        if severity == Severity.SUCCESS:
            return self.successes
        elif severity == Severity.INFO:
            return self.infos
        elif severity == Severity.WARNING:
            return self.warnings
        elif severity == Severity.FAILURE:
            return self.failures
        elif severity == Severity.CRASH:
            return self.crashes
        else:
            raise ValueError("Invalid severity")

    def percentage(self, severity):
        """The percentage of items of severity of the total item count"""
        return 100.0 * self.count(severity) / self.total

    def add(self, severity):
        """Add another item to the checker."""
        if severity == Severity.SUCCESS:
            self.successes += 1
        elif severity == Severity.INFO:
            self.infos += 1
        elif severity == Severity.WARNING:
            self.warnings += 1
        elif severity == Severity.FAILURE:
            self.failures += 1
        elif severity == Severity.CRASH:
            self.crashes += 1
        else:
            raise ValueError("Invalid severity")

    @property
    def total(self):
        """The total number of items."""
        return self.successes + self.infos + self.warnings + self.failures + self.crashes

    @property
    def result(self):
        """True iff the statistics indicate an overall success.
        
        More specifically, true iff there are no failures, warnings or cashes,
        and there are either successes or infos. This implicitly means it is
        false if nothing happened.
        """
        return self.warnings == 0 and self.failures == 0 and self.crashes == 0 and (self.successes != 0 or self.infos != 0)
