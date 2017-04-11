#!/bin/bash

ROOT=$(cd $(dirname $0) && pwd)

# Standardize the script files / the program files into Unix style.
find . -type f -name "*.sh" | xargs chmod +x
find ./test/ai -type f | xargs chmod +x
find . -type f -name "*.sh" | xargs sed -i "s/\r//g"

# Build scripts
./build.sh

# Test start
cd $ROOT && npm install && cd -
mocha $(ls test/test*.js) -t 5300 -R tap
