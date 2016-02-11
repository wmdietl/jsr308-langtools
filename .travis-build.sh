#!/bin/bash

# Fail the whole script if any command fails
set -e

export SHELLOPTS

./.travis-build-without-test.sh

# No testing is done currently
