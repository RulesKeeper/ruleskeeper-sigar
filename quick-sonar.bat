@echo off 

set MAVEN_OPTS="-Xmx1024m" 
call mvn -U clean install sonar:sonar -Dtest=true -DfailIfNoTests=false