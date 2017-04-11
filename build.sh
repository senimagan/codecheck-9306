#!/bin/bash

ROOT=$(cd $(dirname $0) && pwd)

### Java ###
cd $ROOT/java/fw
mvn package -Dmaven.test.skip=true
cd $ROOT/java/ai
mvn package -Dmaven.test.skip=true

### Python ###
# cd $ROOT/python/src
# python -m compileall .

