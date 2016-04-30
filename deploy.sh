#!/usr/bin/env bash
if [ $1 ]
then
    workDir=$PWD
    if [ -f  $workDir/target/test-task-1.0.war ]
    then
        cd $1
        rm -rf webapps/test-task-1.0 webapps/test-task-1.0.war
        cp $workDir/target/test-task-1.0.war webapps
        $1/bin/shutdown.sh
        cd $workDir
        $1/bin/startup.sh
    else
        echo "Файл" $workDir/target/test-task-1.0.war "не найден"
    fi
else
    echo "Передайте католог tomcat в качестве параметра"
fi

