#!/bin/bash

ROOT=$(cd $(dirname $0) && pwd)

./test/ai/weakest_ai "$@"
