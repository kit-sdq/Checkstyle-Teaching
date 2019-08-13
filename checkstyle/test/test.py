import os
import shutil
import subprocess
from json import load, dump
from os.path import dirname
import re
import html2text


def list_examples():
    return list(dict.fromkeys([dirname(dirname(root)) for root, directories, files in os.walk('examples') if
                               'test_sample_solution.py' in files and 'config.json' in files]))


def copy_example(example):
    print('Copying files')

    if os.path.exists('active'):
        shutil.rmtree('active')

    shutil.copytree(example, 'active')


def list_tasks():
    return [root for root, directories, files in os.walk('active')
            if 'test_sample_solution.py' in files and 'config.json' in files]


def read_solution(task):
    solution_path = os.path.join(task, 'solution.html')

    with open(solution_path, 'r') as solution:
        return solution.read()


def test_task_universal(task, command, pythomat):
    config_path = os.path.join(task, 'config.json')

    with open(config_path, 'r') as config:
        json = load(config)
        json['pythomat'] = os.path.relpath(r'pythomat\%s' % pythomat, task)

    with open(config_path, 'w') as config:
        dump(json, config)

    commands = [command, 'test_sample_solution.py', '-o', 'solution.html']
    subprocess.Popen(commands, stdout=subprocess.PIPE, cwd=task).communicate()

    return read_solution(task)


def test_task_test(task):
    return test_task_universal(task, 'python', 'test')


def test_task_reference(task):
    return test_task_universal(task, r'C:\Python27\python.exe', 'reference')


def simplify(solution):
    simplified = html2text.html2text(solution)
    simplified = re.sub(r'Time taken: [0-9]+\.[0-9]+s', 'Time taken: ?s', simplified)
    return simplified


def compare_solutions(task, test_solution, reference_solution):
    reference_simplified = simplify(reference_solution)
    test_simplified = simplify(test_solution)

    if test_simplified == reference_simplified:
        print('Test successful, same as reference.')
        return True
    else:
        print('Test failed, differs from reference. See reference_simplified.txt and test_simplified.txt')

        reference_simplified_path = os.path.join(task, 'reference_simplified.txt')
        test_simplified_path = os.path.join(task, 'test_simplified.txt')

        with open(reference_simplified_path, 'w') as reference_simplified_txt:
            reference_simplified_txt.write(reference_simplified)

        with open(test_simplified_path, 'w') as test_simplified_txt:
            test_simplified_txt.write(test_simplified)

        return False


def test_task(task):
    print('Found task %s' % task)

    test_sample_solution_path = os.path.join(task, 'test_sample_solution.py')

    with open(test_sample_solution_path, 'r') as test_sample_solution:
        with_simple_json = test_sample_solution.read()
        fixed = with_simple_json.replace('import simplejson', 'import json as simplejson')

        if fixed != with_simple_json:
            print('Fixed simplejson')

    with open(test_sample_solution_path, 'w') as test_sample_solution:
        test_sample_solution.write(fixed)

    print('Reference:')
    reference_solution = test_task_reference(task)

    print('Test:')
    test_solution = test_task_test(task)

    return compare_solutions(task, test_solution, reference_solution)


def test_example(example):
    print('Testing %s' % example)
    copy_example(example)

    for task in list_tasks():
        if not test_task(task):
            return False

    return True


for example in list_examples():
    if not test_example(example):
        break
