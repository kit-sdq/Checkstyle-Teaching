import os
import shutil
import subprocess
from json import load, dump
from os.path import dirname


def list_examples():
    return [dirname(dirname(root)) for root, directories, files in os.walk('examples') if
            'test_sample_solution.py' in files and 'config.json' in files]


def copy_example(example):
    print('Copying files')

    if os.path.exists('active'):
        shutil.rmtree('active')

    shutil.copytree(example, 'active')


def list_tasks():
    return [root for root, directories, files in os.walk('active')
            if 'test_sample_solution.py' in files and 'config.json' in files]


def test_example(example):
    print('Testing %s' % example)
    copy_example(example)

    for task in list_tasks():
        print('Found task %s' % task)

        test_sample_solution_path = os.path.join(task, 'test_sample_solution.py')
        config_path = os.path.join(task, 'config.json')

        with open(test_sample_solution_path, 'r') as test_sample_solution:
            with_simple_json = test_sample_solution.read()
            fixed = with_simple_json.replace('import simplejson', 'import json as simplejson')

            if fixed != with_simple_json:
                print('Fixed simplejson')

        with open(test_sample_solution_path, 'w') as test_sample_solution:
            test_sample_solution.write(fixed)

        with open(config_path, 'r') as config:
            json = load(config)
            json['pythomat'] = os.path.relpath(r'pythomat\reference', task)

        with open(config_path, 'w') as config:
            dump(json, config)

        commands = [r'C:\Python27\python.exe', 'test_sample_solution.py', '-o', 'solution.html']
        subprocess.Popen(commands, stdout=subprocess.PIPE, cwd=task).communicate()


for example in list_examples():
    test_example(example)
    break
