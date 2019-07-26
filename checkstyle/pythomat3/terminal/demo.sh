#!/usr/bin/env bash

if [ $# -ne 1 ];
then
  echo "Usage:"
  echo "  ./run.sh {trainer|student|praktomat}"
  echo
  echo "Note that student mode is expected to not work with a fully"
  echo "instrumented solution, while trainer and praktomat mode should provide"
  echo "the same public interface."
  exit 1
fi

if [ ! -d build ]; then mkdir build; fi

mode=$1
shift

echo "Compiling..."
javac -d build $mode/Terminal.java Demonstration.java
if [ $? -ne 0 ]; then exit; fi

echo "Running..."
java -cp build Demonstration $@
