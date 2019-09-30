#!/bin/bash

export DISPLAY=:99
Xvfb ${DISPLAY} -screen 0 1920x1080x24 -nolisten unix &
mvn package -B -U -V --settings .github/settings.xml --file server/pom.xml
