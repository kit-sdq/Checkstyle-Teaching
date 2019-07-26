"""The module exc provides different exceptions.

This includes student's failures, student's warnings and misconfigurations.
"""

class StudentWarning(Exception):
    """Class representing warnings about the studen't submission.

    This exception type should be thrown if the cause of the failure is due to
    a minor programming error by the student.
    """
    def __init__(self, message):
        Exception.__init__(self, message)


class StudentFailure(Exception):
    """Class representing errors in the studen't submission.

    This exception type should be thrown if the cause of the failure is due to
    a programming error by the student.
    """
    def __init__(self, message):
        Exception.__init__(self, message)


class ConfigurationError(Exception):
    """Class representing errors in setting up the tests.

    This exception type should be thrown if the error is caused by the trainer
    when setting the test cases.
    """
    def __init__(self, message):
        Exception.__init__(self, message)
