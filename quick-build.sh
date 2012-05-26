#!/bin/sh
export MAVEN_OPTS='-Xmx512m'

# it is recommended to use maven 3 for faster builds
mvn clean install eclipse:clean eclipse:eclipse -Dtest=false -DfailIfNoTests=false $*
