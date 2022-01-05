#!bin/bash

JAR_PATH=$(ls -t ~/build/*.jar | head -n 1)

JAR_NAME=$(basename $JAR_PATH)

cp ~/build/$JAR_NAME ~/deploy/

echo "[Deploy] : Running new application"

nohup java -jar -Dspring.profiles.active=prod ~/deploy/$JAR_NAME > ~/deploy/nohup.out 2>&1 &

