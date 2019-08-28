#!/bin/bash

mkdir ${HOME}/.ssh
echo "${TRAVIS_SSH_KEY}" > ${HOME}/.ssh/id_rsa
echo "${TRAVIS_SSH_PUB}" > ${HOME}/.ssh/id_rsa.pub
echo 'avereon.com ecdsa-sha2-nistp256 AAAAE2VjZHNhLXNoYTItbmlzdHAyNTYAAAAIbmlzdHAyNTYAAABBBAX0k5tSvrXVpKl7HNPIPglp6Kyj0Ypty6M3hgR783ViTzhRnojEZvdCXuYiGSVKEzZWr9oYQnLr03qjU/t0SNw=' >> ${HOME}/.ssh/known_hosts
chmod 600 ${HOME}/.ssh
chmod 600 ${HOME}/.ssh/id_rsa
chmod 600 ${HOME}/.ssh/id_rsa.pub
chmod 600 ${HOME}/.ssh/known_hosts

scp -B target/*.jar travis@avereon.com:/opt/avn/store/latest/www.avereon.com
scp -B target/main/images/avereon.png travis@avereon.com:/opt/avn/store/stable/avereon/provider.png
