"""Collection of context managers.

Pythomat makes heavy use of python's with statement to ensure everything is
cleaned up nicely. This is for exampled used for making sure HTML output is
well-formed, temporary directories are created and cleaned up, and timeouts are
applied to executions of the student's submission and to the test script
itself.
"""
from collections import deque

import os
import shutil
import signal
import sys
import tempfile
import threading
import time

# There's two different kinds of timeouts:
# If the checker's time is running out, Praktomat raises SIGINT for the process
# and allows it time to exit gracefully before SIGKILL is raised and the
# process is terminated violently.
# The other type of timeout is called by the checker itself to interrupt
# execution of the spawned process when the time runs out for individual tests


class TempDir(object):
    """Context manager for cleanly deleting a temporary directory."""
    def __init__(self):
        self._tempdir = None

    def __enter__(self):
        self._tempdir = tempfile.mkdtemp(prefix="pythomat-")
        return self

    def __exit__(self, type, value, traceback):
        if os.path.exists(self._tempdir):
            # import time
            # time.sleep(60)
            shutil.rmtree(self._tempdir)
        return False

    def __str__(self):
        return self._tempdir

    @property
    def name(self):
        return self._tempdir


class NamedTempDir(object):
    """Context manager for named temporary directories.

    This class is meant to be used in a with statement::

        with NamedTempDir("tmp") as dir:
            # 'dir' can now be used as necessary
        # 'dir' is deleted and cannot be used anymore

    """
    def __init__(self, name):
        """Create a new temporary directory called 'name'."""
        self._name = name

    def __enter__(self):
        os.mkdir(self._name)
        return os.path.abspath(self._name)

    def __exit__(self, type, value, traceback):
        shutil.rmtree(self._name)
        return False

    def __str__(self):
        return self._tempdir


class InternalTimeoutGuard(object):
    """Temporarily install a timer and kill a process if the timer runs out.

    Use this in a with statement::

       process = subprocess.Popen()
       with InternalTimeoutGuard(10, process):
          # do something that may not take more than 10 seconds
       # process should be killed now

    """

    def __init__(self, limit, process):
        """Creatge a new InternalTimeoutGuard.

        limit is in seconds. process is the process to be kill.
        """
        self.limit = limit
        self.process = process
        self.timeout_occured = False
        self.time = 0

    def __enter__(self):

        def timeout_handler():
            self.process.kill()
            self.timeout_occured = True

        self.time = time.time()
        self.timer = threading.Timer(self.limit, timeout_handler)
        self.timer.start()

        return self

    def __exit__(self, type, value, traceback):
        self.timer.cancel()
        self.time = time.time() - self.time

        return False


class ExternalTimeoutException(Exception):
    """Exception being thrown when a Timeout was caused by something outside the script."""
    pass


class ExternalTimeoutGuard(object):
    """Temporarily install a signal handler that catches SIGINT and throws a ExternalTimeoutException."""
    def __init__(self):
        pass

    def __enter__(self):

        def timeout_handler(signum, frame):
            raise ExternalTimeoutException()

        self.previous_int_handler = signal.signal(signal.SIGINT, timeout_handler)
        self.previous_term_handler = signal.signal(signal.SIGTERM, timeout_handler)
        return self

    def __exit__(self, type, value, traceback):
        signal.signal(signal.SIGINT, self.previous_int_handler)
        signal.signal(signal.SIGTERM, self.previous_term_handler)
        return False


class IllegalStreamAccessException(Exception):
    """Report an attempted access to a stream that should not be used at this
    point of time.

    Primarily used to make sure sys.stdout is not used except via the
    html.Document class.
    """
    def __str__(self):
        return "Illegal output or error stream access"


class CaptureStdoutGuard(object):
    """Store everything written sys.stdout for later use.

    It can then be accessed through the attribute lines."""
    def __init__(self):
        self.lines = ""

    def write(self, lines):
        self.lines += lines

    def __enter__(self):
        self.old = sys.stdout
        sys.stdout = self
        return self

    def __exit__(self, type, value, traceback):
        sys.stdout = self.old
        return False


class NoStdoutGuard(object):
    """Temporarily disable standard output (e.g. print)

    This is e.g. used in combination with html.Document to make sure the html
    code is always well-formed."""
    def __init__(self):
        pass

    def __enter__(self):

        class NullStream():
            def write(self, string):
                raise IllegalStreamAccessException()

        self.stdout = sys.stdout
        sys.stdout = NullStream()
        return self

    def __exit__(self, type, value, traceback):
        sys.stdout = self.stdout
        return False


class NoStderrGuard(object):
    """Temporarily disable the standard error stream."""
    def __init__(self):
        pass

    def __enter__(self):
        class NullStream():
            def write(self, string):
                raise IllegalStreamAccessException()

        self.stderr = sys.stderr
        sys.stderr = NullStream()
        return self

    def __exit__(self, type, value, traceback):
        sys.stderr = self.stderr
        return False# this is shamelessly copied from contextlib2, but the license permits it


class BufferedStdoutGuard(object):
    """Buffer everything writte to sys.stdout until the context is left.

    This can be used to make it less likely that non-well-formed html is
    written to stdout."""
    def __init__(self):
        pass

    def __enter__(self):
        class StreamBuffer():
            def __init__(self):
                self.content = ""

            def write(self, string):
                self.content += string

            def flush(self):
                pass

        self.stdout = sys.stdout
        sys.stdout = StreamBuffer()
        return self

    def __exit__(self, type, value, traceback):
        content = sys.stdout.content
        sys.stdout = self.stdout
        sys.stdout.write(content)
        sys.stdout.flush()


# this is c&p from libcontext2 (python-2.x doesn't have this, yet)
class ExitStack(object):
    """Context manager for dynamic management of a stack of exit callbacks

    For example:

        with ExitStack() as stack:
            files = [stack.enter_context(open(fname)) for fname in filenames]
            # All opened files will automatically be closed at the end of
            # the with statement, even if attempts to open files later
            # in the list throw an exception

    This is copy and pasted from libcontext2, which afair is a backport of
    python3 code.
    """
    def __init__(self):
        self._exit_callbacks = deque()

    def pop_all(self):
        """Preserve the context stack by transferring it to a new instance"""
        new_stack = type(self)()
        new_stack._exit_callbacks = self._exit_callbacks
        self._exit_callbacks = deque()
        return new_stack

    def _push_cm_exit(self, cm, cm_exit):
        """Helper to correctly register callbacks to __exit__ methods"""
        def _exit_wrapper(*exc_details):
            return cm_exit(cm, *exc_details)
        _exit_wrapper.__self__ = cm
        self.push(_exit_wrapper)

    def push(self, exit):
        """Registers a callback with the standard __exit__ method signature

        Can suppress exceptions the same way __exit__ methods can.

        Also accepts any object with an __exit__ method (registering the
        method instead of the object itself)
        """
        # We use an unbound method rather than a bound method to follow
        # the standard lookup behaviour for special methods
        _cb_type = type(exit)
        try:
            exit_method = _cb_type.__exit__
        except AttributeError:
            # Not a context manager, so assume its a callable
            self._exit_callbacks.append(exit)
        else:
            self._push_cm_exit(exit, exit_method)
        return exit # Allow use as a decorator

    def callback(self, callback, *args, **kwds):
        """Registers an arbitrary callback and arguments.

        Cannot suppress exceptions.
        """
        def _exit_wrapper(exc_type, exc, tb):
            callback(*args, **kwds)
        # We changed the signature, so using @wraps is not appropriate, but
        # setting __wrapped__ may still help with introspection
        _exit_wrapper.__wrapped__ = callback
        self.push(_exit_wrapper)
        return callback # Allow use as a decorator

    def enter_context(self, cm):
        """Enters the supplied context manager

        If successful, also pushes its __exit__ method as a callback and
        returns the result of the __enter__ method.
        """
        # We look up the special methods on the type to match the with statement
        _cm_type = type(cm)
        _exit = _cm_type.__exit__
        result = _cm_type.__enter__(cm)
        self._push_cm_exit(cm, _exit)
        return result

    def close(self):
        """Immediately unwind the context stack"""
        self.__exit__(None, None, None)

    def __enter__(self):
        return self

    def __exit__(self, *exc_details):
        if not self._exit_callbacks:
            return
        # This looks complicated, but it is really just
        # setting up a chain of try-expect statements to ensure
        # that outer callbacks still get invoked even if an
        # inner one throws an exception
        def _invoke_next_callback(exc_details):
            # Callbacks are removed from the list in FIFO order
            # but the recursion means they're invoked in LIFO order
            cb = self._exit_callbacks.popleft()
            if not self._exit_callbacks:
                # Innermost callback is invoked directly
                return cb(*exc_details)
            # More callbacks left, so descend another level in the stack
            try:
                suppress_exc = _invoke_next_callback(exc_details)
            except:
                suppress_exc = cb(*sys.exc_info())
                # Check if this cb suppressed the inner exception
                if not suppress_exc:
                    raise
            else:
                # Check if inner cb suppressed the original exception
                if suppress_exc:
                    exc_details = (None, None, None)
                suppress_exc = cb(*exc_details) or suppress_exc
            return suppress_exc
        # Kick off the recursive chain
        return _invoke_next_callback(exc_details)

