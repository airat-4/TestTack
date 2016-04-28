cd ~/Apps/apache-tomcat-8.0.24/webapps
rm -rf ./test-task-1.0 ./test-task-1.0.war
cp ~/Apps/Idea/TestTack/target/test-task-1.0.war ./
shutdown.sh
cd ~/Apps/Idea/TestTack
startup.sh
