@echo off 

set MAVEN_OPTS="-Xmx1024m"
call mvn eclipse:clean clean
call generate.bat 
call mvn eclipse:eclipse package install -DskipTests -DdownloadSources=false -DdownloadDocs=false