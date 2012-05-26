set MAVEN_OPTS=-Xmx768m -XX:MaxPermSize=256m
mvn clean install eclipse:clean eclipse:eclipse -Dtest=false -DfailIfNoTests=false -Psanity-checks %*
