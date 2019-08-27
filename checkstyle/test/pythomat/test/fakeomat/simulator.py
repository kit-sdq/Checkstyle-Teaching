import os.path
import subprocess
import sys
import tempfile
import traceback
import webbrowser

from pythomat import contexts
from pythomat import html
from .checkers import SearchMainDummyChecker
from .checkers import SubmissionFailure
from .checkers import parse_checker_lines
from .submission import CopySubmissionChecker
from .submission import FindJava
from .submission import SearchMainMethodChecker

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

_toggle_script = """
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
        sys.stderr.write(checker.title() + ' ...')
        checker.decorated_run(document, tempdir)

    except SubmissionFailure:
        sys.stderr.write(' failed\n')
        return 1

    except Exception as e:
        sys.stderr.write(' crashed (' + str(e) + ')\n')
        return 1

    else:
        sys.stderr.write(' done\n')

    return 0


def run_checkers(checkers, document, tempdir):
    """Try to figure out what to do with the checker files and run them"""

    # TODO counting
    failures = sum(1 for checker in checkers if run_checker(checker, document, tempdir))

    if failures != 0:
        raise SubmissionFailure(str(failures) + ' checker(s) failed')


# TODO unused argument student_submission
def fake_praktomat(document, tempdir, student_submission, checkers):
    with document.html():
        with document.head():
            with document.title():
                document.cdata('Fake Praktomat Output')

            document.css(_css)
            document.script({'src': 'http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js'})
            document.script({'src': 'http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery-ui.min.js'})

            with document.javascript():
                document.cdata(_toggle_script)

            with document.meta({'charset': 'utf-8'}):
                pass

        with document.body():
            try:
                try:
                    with document.h2():
                        document.cdata('Results')

                    run_checkers(checkers, document, tempdir)
                finally:
                    with document.h2():
                        document.cdata('Summary')

            except SubmissionFailure as e:
                with document.div({'class': 'failed'}):
                    document.cdata(str(e))

            except Exception as e:
                with document.div({'class': 'failed'}):
                    document.cdata(str(e))

                with document.div({'style': "white-space:pre"}):
                    document.cdata(traceback.format_exc())

            else:
                with document.div({'class': 'passed'}):
                    document.cdata('All checkers ran successfully')


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
        raise UserInputError('Need to specify a submission')

    checkers = parse_checker_lines(checkers)

    # Check if the main method search should be at a manual point in the pipeline
    found = False
    for i, checker in enumerate(checkers):
        if isinstance(checker, SearchMainDummyChecker):
            checkers[i] = SearchMainMethodChecker('')
            found = True

    default_checkers = [
        FindJava(''),
        CopySubmissionChecker(submission),
    ]

    # Add search for main method to the front if not found for manual execution
    if not found:
        default_checkers.append(SearchMainMethodChecker(''))

    checkers = default_checkers + checkers

    if output == 'stdout':
        return guarded_fakeomat(sys.stdout, submission, checkers)

    elif output == 'browser':
        with tempfile.NamedTemporaryFile(suffix='.html') as tmp:
            guarded_fakeomat(tmp, submission, checkers)
            tmp.flush()
            webbrowser.open_new_tab(tmp.name)

            try:
                input('Press any key to continue\n')
            except (KeyboardInterrupt, EOFError):
                pass

    elif output == 'chromium':
        with tempfile.NamedTemporaryFile(suffix='.html') as tmp:
            guarded_fakeomat(tmp, submission, checkers)
            tmp.flush()

            with open(os.devnull, 'wb') as devnull:
                browser = subprocess.Popen(['chromium-browser', '--incognito', tmp.name], stdout=devnull,
                                           stderr=devnull)

                try:
                    browser.wait()
                except KeyboardInterrupt:
                    pass

    elif output == 'firefox':
        # firefox detaches from the shell so we cannot wait for it to terminate
        with tempfile.NamedTemporaryFile(suffix='.html') as tmp:
            guarded_fakeomat(tmp, submission, checkers)
            tmp.flush()

            with open(os.devnull, 'wb') as devnull:
                subprocess.Popen(['firefox', '--new-window', tmp.name], stdout=devnull, stderr=devnull)

                # we cannot wait for the browser window, because that firefox instance might terminate instantly
                # we wait for the user to press a key instead
                try:
                    input('Press any key to continue\n')
                except (KeyboardInterrupt, EOFError):
                    pass

    elif output.endswith('.html'):
        with open(output, 'w') as output_file:
            return guarded_fakeomat(output_file, submission, checkers)

    else:
        raise UserInputError('Invalid output')

    return True
