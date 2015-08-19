#!/bin/bash
cd make
ant

echo Triggering build of typetools/annotation-tools
curl -s https://raw.githubusercontent.com/mernst/plume-lib/master/bin/trigger-travis.sh > trigger-travis.sh
bash trigger-travis.sh typetools annotation-tools $TRAVISTOKEN
rm trigger-travis.sh

