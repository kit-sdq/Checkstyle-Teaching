#!/bin/bash
for f in *.cnf; do name=`basename $f .cnf`; echo; echo $name; java -cp ../../../../SS13/assignments/final01/solution/bin dp.DirectionalResolution $f > $name.log; done
