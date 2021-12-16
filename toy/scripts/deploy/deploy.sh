#!bin/bash

JAR_NAME=toy-0.0.1-SNAPSHOT.jar

cp ~/build/$JAR_NAME ~/deploy/

CURRENT_PID=$(pgrep -f $JAR_NAME)

if [[ -z $CURRENT_PID ]];
then
    echo "[Deploy] : No application currently running"
else
    echo "[Deploy] : Application already running"
    echo "[Deploy] : Stopping existing application"
    kill -15 $CURRENT_PID
    sleep 5
fi

echo "[Deploy] : Running new application"

nohup java -jar -Dspring.profiles.active=prod ~/deploy/$JAR_NAME > ~/deploy/nohup.out 2>&1 &
