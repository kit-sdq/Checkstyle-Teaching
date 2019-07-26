#!/usr/bin/env python

from distutils import core

def setup():
    core.setup(
        name='pythomat',
        version='0.1.0',
        author='Florian Merz',
        author_email='florian.merz@kit.edu',
        packages=['fakeomat','pythomat'],
        url='http://baldur.iti.kit.edu/programmieren/',
    #    license='LICENSE.txt',
        description='Useful Praktomat related python code.',
        long_description=open('README.txt').read()
    )

if __name__ == "__main__":
    setup()
