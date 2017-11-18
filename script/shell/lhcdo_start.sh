#基础目录
BASE_HOME=/data/applications/lhcdo
LOG_HOME=/data/logs/lhcdo

#启动
pushd ${BASE_HOME}/bin
nohup java -jar -Dspring.profiles.active=prod lhcdo.jar > ${LOG_HOME}/lhcdo.log &
popd


