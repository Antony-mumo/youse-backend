echo '===> Compiling & Packaging youse ...'
mvn clean install -Dmaven.tests.skip=true
docker build -t yousebe .