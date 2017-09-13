#!/bin/bash

# Fail the whole script if any command fails
set -e

export SHELLOPTS

cd make
## "Ant" is itself a script that runs some commands that fail.
set +e
ant
set -e

