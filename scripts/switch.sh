#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

function switch_proxy() {
  IDLE_PORT=$(find_idle_port)

  # 하나의 문장을 만들어 파이프라인(|)로 넘겨주기 위해 echo 사용
  # 엔진엑스가 변경할 프록시 주소 생성, "" 사용해야 함 안 그러면 $service_url 인식을 못함
  # sudo tee ~는 앞에서 넘겨준 문장을 service_url.inc에 덮어씀
  echo "> 전환할 Port: $IDLE_PORT"
  echo "> Port 전환"
  echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc

  # 엔진엑스 설정을 다시 불러옴, restart와는 다름(restart : 잠깐 끊김 현상 존재, reload : 계속 불러옴)
  # 중요 설정들 반영은 restart 이용(여기서는 외부 설정 파일을 다시 불러오는거라 reload)
  echo "> 엔진엑스 Reload"
  sudo service nginx reload
}