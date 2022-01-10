#!bin/bash

JAR_PATH=$(ls -t ~/build/*.jar | head -n 1)

JAR_NAME=$(basename $JAR_PATH)

RESOURCES_PATH=/home/ec2-user/deploy/resources

cp ~/build/$JAR_NAME ~/deploy/

echo "[Deploy] : Running new application"

nohup java -jar -Dspring.profiles.active=prod \
        -Dspring.config.additional-location=$RESOURCES_PATH/application-oauth.yml,$RESOURCES_PATH/application-aws.yml \
        ~/deploy/$JAR_NAME > ~/deploy/nohup.out 2>&1 &

