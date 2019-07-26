The Terminal class
==================

The Terminal class provides easy means to create test cases for interactive
programs. For this there are three variations of the class: student, trainer,
and praktomat, and two kinds of protocols: 'expected' and 'observed' protocols.
'expected' protocols are create by the trainer Terminal class and used by the
praktomat Terminal class. This one then creates an 'observed' protocol. This
protocol provides all necessary information about a student's submission's run.

* student
    Implementation to hand out to students
* trainer
    Implementation for creating 'expected' protocols
* praktomat
    Implementation to be run on the praktomat server to create 'observed'
    protocols

The Terminal class for students provides two methods to the students:
- void Terminal.println(String) for output
- String Terminal.readln() for user input

The Terminal class for the trainer provides additional methods that can be
used to create richer protocols:
- void Terminal.printError(String) to mark outout as an error message
- void Terminal.printInfo(String) to print additional info for the students
- void Terminal.exit(int) to add the expected exit code to the protocol
This implementation also protocols program termination via SIGTERM.
The protocol is written to the file 'protocol.txt' in the current working
directory.

Finally, the Terminal class for praktomat expects a 'protocol.txt' file in
the current working directory. It parses this file and generates a list of
expected events from that file. When its println, println and readln methods
are called it compares these events to the expected events from the protocol.
The implementation prints out another protocol, the 'observed' protocol to
its standard output, which documents the student's program run.

Furthermore, praktomat's Terminal class uses ShutdownHooks, System.setIn(),
System.setOut() and a Timer for further instrumentation.


Event types
===========

An event line in a protocol contains of type and text separated by a colon.
The type may not contain a colon itself, but text may. Events with multi-line
text are not allowed. Ever. They are automatically split up into multiple
single line events of the same type.

The following event types exist:

input:text
  'expect'
      A call to readLine is expected. If it does happen, text will be used as
      the result. The string 'EOF' is special cased to mean end-of-file.
  'observed'
      A call to readLine did happen and text is the call's result.

output:text
  'expected'
      A call to printLine() is expected. If it does happen, text is the
      expected argument of the call. Leading and trailing whitespace are
      ignored. Note that an error message is not expected here. See below
      for error messages.
  'observed'
      A call to printLine() did happen with 'text' as the argument.

info:text
  'expected' and observed'
      If the submission gets to this point an info box shall be display.

error:text
  'expected'
      Printing of an error message is expected. An error message is any string
      that starts with 'error,', 'error!', or 'error:'.
  'observed'
      The student's submission did not do what was expected. text is an error
      message describing the problem.

crash:text
  'expected'
      This event type is not expected to occur in an 'expect' protocol.
  'observed'
      The Terminal class itself crashed for internal reasons. This is not the
      student's fault, but the trainer's.

timeout:text
  'expected'
      Set the timeout that should be used on the praktomat server. text is
      expected to be an integer.
  'observed'
      A timeout occured before the submission finished.

term:text
  'expected'
      The trainer pressed ctrl+c to abort the test, usually because he is not
      interested in anything beyond this point. The test on the praktomat
      should also be aborted at this point.
  'observed'
      This type of event cannot be observed.

exit:text
  'expected'
      A program exit with the indicated return code is expected.
  'observed'
      A program exit was observed and also expected. text is the expected
      return code. The real return code needs to be compared to this by the
      caller, if needed.


TODO
====

* Write a minimal pythomat-independent script for Praktomat using the Terminal
  class.
* Cover all features in the demo
* Print out the remaining output lines after something failed. Only stop at the
  next readline. Possibly indicate to the script that this is to be grayed out,
  meaning it is not checked against the protocol.
