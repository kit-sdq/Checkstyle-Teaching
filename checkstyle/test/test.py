import json
import os
import re
import shutil
import subprocess
from os.path import dirname, join, relpath, exists

import html2text

test_command = 'python'
reference_command = r'C:\Python27\python.exe'


def is_task(files):
    """Checks if the given list of files contains test_sample_solution.py and config.json."""

    return 'test_sample_solution.py' in files and 'config.json' in files


def list_tasks(path):
    """Returns a list of all tasks under the given path."""

    return [root for root, directories, files in os.walk(path) if is_task(files)]


def list_examples(path):
    """Returns a list of all examples under the given path. An example may contain multiple tasks."""

    examples_with_duplicates = [dirname(dirname(task)) for task in list_tasks(path)]
    return list(dict.fromkeys(examples_with_duplicates))


def copy_example(example_path):
    """Copies the given example to /active so that the original directories don't get polluted."""

    if exists('active'):
        shutil.rmtree('active')

    print('Copying example to active')
    shutil.copytree(example_path, 'active')


def fix_simplejson_if_necessary(task_path):
    """
    Modifies a task's test_sample_solution.py to use 'json' as 'simplejson' if necessary.
    This step is required to run with Python 3.X.
    """

    test_sample_solution_path = join(task_path, 'test_sample_solution.py')

    with open(test_sample_solution_path, 'r') as test_sample_solution:
        with_simple_json = test_sample_solution.read()
        fixed = with_simple_json.replace('import simplejson', 'import json as simplejson')

        if fixed != with_simple_json:
            print('Fixed simplejson')

    with open(test_sample_solution_path, 'w') as test_sample_solution:
        test_sample_solution.write(fixed)


# noinspection PyIncorrectDocstring
def run_task(task_path, command, pythomat):
    """
    Runs a task's test_sample_solution.py. To achieve that the corresponding config.json has to be modified.

    :param command: Python interpreter.
    :param pythomat: Either 'test' or 'reference'.
    """

    config_path = join(task_path, 'config.json')

    with open(config_path, 'r') as config_handle:
        config = json.load(config_handle)
        config['pythomat'] = relpath(join('pythomat', pythomat), task_path)

    with open(config_path, 'w') as config_handle:
        json.dump(config, config_handle)

    commands = [command, 'test_sample_solution.py', '-o', 'solution.html']
    subprocess.Popen(commands, cwd=task_path).communicate()


def get_solution(task_path):
    """Reads and deletes a task's solution.html."""

    solution_path = join(task_path, 'solution.html')

    with open(solution_path, 'r') as handle:
        solution = handle.read()

    os.remove(solution_path)
    return solution


def simplify(solution):
    simplified = html2text.html2text(solution)
    simplified = simplified.replace('\n', '')
    simplified = re.sub(r'Time taken: [0-9]+\.[0-9]+s', 'Time taken: ?s', simplified)
    simplified = re.sub('pythomat-[a-z0-9_]{6}', 'pythomat-?', simplified)
    simplified = simplified.replace(' ', '')
    return simplified


def compare_solutions(reference_solution, test_solution):
    """
    Compares a reference and test solution.

    :return: true iff the solutions are equivalent.
    """

    test_simplified = simplify(test_solution)
    reference_simplified = simplify(reference_solution)

    with open('test_simplified.txt', 'w') as handle:
        handle.write(test_simplified)

    with open('reference_simplified.txt', 'w') as handle:
        handle.write(reference_simplified)

    if reference_simplified == test_simplified:
        print('Test successful')
        return True
    else:
        print('Test not successful')
        return False


def test_task(task_path):
    """
    Tests a task. First the task is run with the reference and test Pythomat.
    After that their corresponding solutions are compared.

    :return: true iff the test was successful.
    """

    print('Found task {}'.format(task_path))
    fix_simplejson_if_necessary(task_path)

    print('\nTest:')
    run_task(task_path, test_command, 'test')
    test_solution = get_solution(task_path)

    with open('test.html', 'w') as handle:
        handle.write(test_solution)

    print('\nReference:')
    run_task(task_path, reference_command, 'reference')
    reference_solution = get_solution(task_path)

    with open('reference.html', 'w') as handle:
        handle.write(reference_solution)

    print('\n')

    return compare_solutions(reference_solution, test_solution)


def test_example(example_path):
    """
    Copies the given example to /active and tests all it's tasks.

    :return: true iff all test were successful.
    """

    print('Testing %s' % example_path)
    copy_example(example_path)

    for task_path in list_tasks('active'):
        if not test_task(task_path):
            return False

    return True


def test():
    examples = list_examples('examples')

    for example in examples:
        print('Example {} of {}'.format(examples.index(example) + 1, len(examples)))

        if not test_example(example):
            break

        print('\n')


test()
