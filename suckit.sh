#!/bin/bash
git pull origin master
mvn clean install
./deploy.sh

