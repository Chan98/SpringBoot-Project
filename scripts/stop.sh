#!/usr/bin/env bash

# 현재 stop.sh가 속해 잇는 경로 찾음
# 하단의 코드와 같이 profile.sh의 경로 찾기 위해 사용
ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh # 자바의 import 구문과 비슷, profile의 function 사용 가능

IDLE_PORT=$(find_idle_port)

echo "> $IDLE_PORT 에서 구동중인 애플리케이션 pid 확인"
IDLE_PID=$(lsof -ti tcp:${IDLE_PORT})

if [ -z ${IDLE_PID} ]
then
echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
echo "> kill -15 $IDLE_PID"
kill -15 ${IDLE_PID}
sleep 5
fi