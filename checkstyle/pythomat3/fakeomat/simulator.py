import os.path
import re
import shutil
import subprocess
import sys
import tempfile
import traceback
import webbrowser
import zipfile

from . import packager
from .checkers import CheckerFailure
from .checkers import SubmissionFailure
from .checkers import parse_checker_lines
from .checkers import SearchMainDummyChecker
from .submission import FindJava
from .submission import CopySubmissionChecker
from .submission import SearchMainMethodChecker
from pythomat import contexts
from pythomat import html


_css = """
* {
  font-family:sans-serif
}

.failed {
  color:red
}

.passed {
  color:green
}

.togglebutton {
  cursor: pointer;
}


.togglesymbol {
  width: 2em;
  text-align: center;
  display: inline-block;
}

.hideable {
  overflow-y: hidden;
  overflow-x: auto;
}
"""

_togglescript = """
function slide(button, element) {
    if ($(button).text() == '-') {
        $(element).slideUp();
        button.innerHTML = '+';
    } else {
        $(element).slideDown();
        button.innerHTML = '-';
    }
}
"""

def run_checker(checker, document, tempdir):
    """Run a checker and print status on the command line"""
    try:
        sys.stderr.write(checker.title() + " ...")
        checker.decorated_run(document, tempdir)

    except SubmissionFailure as e:
        sys.stderr.write(" failed\n")
        return 1

    except Exception as e:
        sys.stderr.write(" crashed (" + str(e) + ")\n")
        return 1

    else:
        sys.stderr.write(" done\n")

    return 0


def run_checkers(checkers, document, tempdir):
    '''Try to figure out what to do with the checker files and run them'''
    failures = sum(1 for checker in checkers if run_checker(checker, document, tempdir))
    if failures != 0:
        raise SubmissionFailure(str(failures) + " checker(s) failed")


def fake_praktomat(document, tempdir, student_submission, checkers):
    with document.html():
        with document.head():
            with document.title():
                document.cdata("Fake Praktomat Output")
            document.css(_css)
            document.script({'src':'http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js'})
            document.script({'src':'http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery-ui.min.js'})
            with document.javascript():
                document.cdata(_togglescript)
            with document.meta({ "charset" : "utf-8" }):
                pass
        with document.body():
            try:
                try:
                    # run the checkers
                    with document.h2():
                        document.cdata("Results")
                    run_checkers(checkers, document, tempdir)

                finally:
                        with document.h2():
                            document.cdata("Summary")

            except SubmissionFailure as e:
                with document.div({'class' : 'failed'}):
                    document.cdata(str(e))

            except Exception as e:
                with document.div({'class' : 'failed'}):
                    document.cdata(str(e))
                with document.div({'style':"white-space:pre"}):
                    document.cdata(traceback.format_exc())

            else:
                with document.div({'class' : 'passed'}):
                    document.cdata("All checkers ran successfully")


def guarded_fakeomat(output_stream, submission, checkers):
    with html.Document(output_stream) as document:
        with contexts.NoStdoutGuard():
            with contexts.ExternalTimeoutGuard():
                with contexts.TempDir() as tempdir:
                    fake_praktomat(document, tempdir.name, submission, checkers)


class UserInputError(Exception):
    pass


def run(checkers, submission, output):
    if not submission:
        raise UserInputError("Need to specify a submission")

    # parse the checker lines and create Checkers accordingly
    checkers = parse_checker_lines(checkers)

    checkerlist = [
        FindJava(""),
        CopySubmissionChecker(submission),
    ]

    # Check if the main method search should be at a manual point in the pipeline
    found = False
    for idx, checker in enumerate(checkers):
        if isinstance(checker, SearchMainDummyChecker):
            checkers[idx] = SearchMainMethodChecker("")
            found = True

    # Add search for main method to the front if not found for manual execution
    if not found:
        checkerlist.append(SearchMainMethodChecker(""))

    checkers = checkerlist + checkers

    # write to sys.stdout
    if output == 'stdout':
        return guarded_fakeomat(sys.stdout, submission, checkers)

    # run automatically detected browser
    elif output == 'browser':
        with tempfile.NamedTemporaryFile(suffix='.html') as tmp:
            guarded_fakeomat(tmp, submission, checkers)
            tmp.flush()
            webbrowser.open_new_tab(tmp.name)
            try:
                eval(input("Press any key to continue\n"))
            except (KeyboardInterrupt, EOFError):
                pass

    # run chromium
    elif output == 'chromium':
        # stream result to tempfile and open that in chromium
        with tempfile.NamedTemporaryFile(suffix=".html") as tmp:
            # delete tempfile as soon as the browser is closed
            guarded_fakeomat(tmp, submission, checkers)
            tmp.flush()
            with open(os.devnull, 'wb') as devnull:
                browser = subprocess.Popen(["chromium-browser", "--incognito", tmp.name], stdout=devnull, stderr=devnull)
                try:
                    browser.wait()
                except KeyboardInterrupt:
                    pass

    # run firefox
    elif output == 'firefox':
        # firefox detaches from the shell so we cannot wait for it to terminate
        with tempfile.NamedTemporaryFile(suffix=".html") as tmp:
            # delete tempfile as soon as the browser is closed
            guarded_fakeomat(tmp, submission, checkers)
            tmp.flush()
            with open(os.devnull, 'wb') as devnull:
                browser = subprocess.Popen(["firefox", "--new-window", tmp.name], stdout=devnull, stderr=devnull)
                # we cannot wait for the browser window, because that firefox instance might terminate instantly
                # we wait for the user to press a key instead
                try:
                    eval(input("Press any key to continue\n"))
                except (KeyboardInterrupt, EOFError):
                    pass

    # write to html file output
    elif output.endswith(".html"):
        # stream result to the specified output file
        with open(output, 'w') as output_file:
            return guarded_fakeomat(output_file, submission, checkers)

    # everything else is not allowed
    else:
        raise UserInputError("Invalid output")

    return True
