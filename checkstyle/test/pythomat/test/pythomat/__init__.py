# configure logging of test results, if requested by the calling script
# logging actually takes place in ipo.py
import os
if os.environ.get('PYTHOMAT_TESTS_LOG'):
    import logging
    log_file = os.environ.get('PYTHOMAT_TESTS_LOG')
    logging.basicConfig(filename=log_file, level=logging.DEBUG, format='%(message)s')

__all__ = [
    'analysers.py',
    'checker.py',
    'checkstyle.py',
    'contexts.py',
    'css.py',
    'filenames.py',
    'html.py',
    'interactive.py',
    'output.py',
    'patterns.py',
    'pseudo_interactive.py',
    'submission.py',
    'test.py',
]
