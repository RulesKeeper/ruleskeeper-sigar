@echo off 

set MAVEN_OPTS="-Xmx1024m" 
call mvn eclipse:clean eclipse:eclipse clean package install -DskipTests -DdownloadSources=false -DdownloadDocs=false