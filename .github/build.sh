#!/bin/bash

rm client.zip
cd client
npm install-clean
npm run-script build
cd build
zip -r ../client.zip *
cd ..
cd ..

mvn ${{env.MAVEN_PARMS}} package
