#!/bin/bash

#Xvfb ${DISPLAY} -screen 0 1920x1080x24 -nolisten unix &
#mvn package -B -U -V --settings .github/settings.xml --file pom.xml

mkdir ${HOME}/.ssh
#chmod 600 ${HOME}/.ssh
echo "${TRAVIS_SSH_KEY}" > ${HOME}/.ssh/rsa_id
echo "${TRAVIS_SSH_PUB}" > ${HOME}/.ssh/rsa_id.pub
echo 'avereon.com ecdsa-sha2-nistp256 AAAAE2VjZHNhLXNoYTItbmlzdHAyNTYAAAAIbmlzdHAyNTYAAABBBAX0k5tSvrXVpKl7HNPIPglp6Kyj0Ypty6M3hgR783ViTzhRnojEZvdCXuYiGSVKEzZWr9oYQnLr03qjU/t0SNw=' >> ${HOME}/.ssh/known_hosts
#chmod 600 ${HOME}/.ssh/*

#scp -B target/*.jar travis@avereon.com:/opt/avn/store/latest/www.avereon.com
#scp -B target/main/images/avereon.png travis@avereon.com:/opt/avn/store/stable/avereon/provider.png

ls -al ${HOME}/.ssh
echo 'test' > test.txt
scp -B test.txt travis@avereon.com:test.txt
