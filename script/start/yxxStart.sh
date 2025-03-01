#!/bin/sh

PROJECT_PATH=/usr/local/yxx/yxx-yi
APP_NAME=yxx-yxx
JAR_NAME=$APP_NAME.jar
LOG_PATH=$PROJECT_PATH/$JAR_PATH


echo 停止原来运行中的工程
tpid=`ps -ef|grep $APP_NAME|grep -v grep|grep -v kill|awk '{print $2}'`
if [ ${tpid} ]; then
    echo 'Stop Process...'
    kill -15 $tpid
fi
sleep 2
tpid=`ps -ef|grep $APP_NAME|grep -v grep|grep -v kill|awk '{print $2}'`
if [ ${tpid} ]; then
    echo 'Kill Process!'
    kill -9 $tpid
else
    echo 'Stop Success!'
fi

cd $PROJECT_PATH

echo 启动项目
nohup java -jar -Xms256m -Xmx512m -jar -Dspring.profiles.active=test $JAR_NAME &> $LOG_PATH/$APP_NAME.log &
echo 项目启动完成

timeout 40s tail -f -n200 $LOG_PATH/$APP_NAME.log
