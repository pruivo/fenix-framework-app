#!/bin/bash

WORKING_DIR=`cd $(dirname $0); pwd`
TARGET_DIR=`cd ${WORKING_DIR}/../target; pwd`

#constants...
CLASS="eu.cloudtm.ServerMain"

#vars...
JMX_PORT=9999;
JMX_ENABLED=false
LCRD_LOAD_BALANCE="false"

while [ -n "$1" ]; do
  case $1 in
    --enable-jmx) JMX_ENABLED="true";;
    --jmx-port) JMX_PORT=$2; shift 1;;
    --lcrd) LCRD_LOAD_BALANCE="true";;
    *) JAVA_ARGS="$JAVA_ARGS $1";;
  esac
  shift 1;
done


#fenix framework
D_VARS="-Dfenixframework.appName=server"
D_VARS="$D_VARS -Dfenixframework.domainModelURLs=fenix-framework-domain-root.dml,books.dml"
D_VARS="$D_VARS -Dfenixframework.ispnConfigFile=ispn.xml"
D_VARS="$D_VARS -Dfenixframework.coreThreadPoolSize=1"
D_VARS="$D_VARS -Dfenixframework.maxThreadPoolSize=2"
D_VARS="$D_VARS -Dfenixframework.keepAliveTime=60000"
D_VARS="$D_VARS -Dfenixframework.messagingJgroupsFile=jgroups-ff.xml"
D_VARS="$D_VARS -Dfenixframework.jGroupsConfigFile=jgroups-ff.xml"
D_VARS="$D_VARS -Dfenixframework.useGrouping=true"
D_VARS="$D_VARS -Dfenixframework.expectedInitialNodes=1"

if [ "$LCRD_LOAD_BALANCE" == "true" ]; then
D_VARS="$D_VARS -Dfenixframework.loadBalancePolicyClass=pt.ist.fenixframework.backend.infinispan.messaging.lcdr.LCRDLoadBalancePolicy"
D_VARS="$D_VARS -DautomaticLocalityHints=true"
D_VARS="$D_VARS -DuseTxClass=true"
fi

HOSTNAME=$(hostname)
#HOSTNAME="127.0.0.1"

#jgroups IPv4
D_VARS="$D_VARS -Djava.net.preferIPv4Stack=true -Djgroups.bind_addr=$HOSTNAME"

#log4j
D_VARS="$D_VARS -Dlog4j.configuration=file:$WORKING_DIR/log4j.properties"

if [ "$JMX_ENABLED" == "true" ]; then
#jmx remote
D_VARS="$D_VARS -Dcom.sun.management.jmxremote"
D_VARS="$D_VARS -Dcom.sun.management.jmxremote.authenticate=false"
D_VARS="$D_VARS -Dcom.sun.management.jmxremote.ssl=false"
D_VARS="$D_VARS -Dcom.sun.management.jmxremote.port=$JMX_PORT"
fi

#Jprofiler
#JP_AGENT="-agentpath:/opt/jprofiler/bin/linux-x64/libjprofilerti.so"

#JVM tunning
JVM="-Xmx2G -Xms1G"
#JVM="$JVM -XX:SurvivorRatio=6"
#JVM="$JVM -XX:NewSize=512m"
#JVM="$JVM -XX:MaxNewSize=1G"
#JVM="$JVM -XX:MaxPermSize=64m"
#JVM="$JVM -XX:NewRatio=2"
#JVM="$JVM -XX:+UseConcMarkSweepGC"
#JVM="$JVM -XX:+CMSIncrementalMode"
#JVM="$JVM -XX:+UseParNewGC"

#class path
CP="$TARGET_DIR/fenix-framework-app-1.0.jar:$WORKING_DIR"

for jar in `ls ${TARGET_DIR}/dependency/*.jar`; do
    CP="$CP:$jar";
done

CMD="java $JVM $JP_AGENT $D_VARS -cp $CP $CLASS $JAVA_ARGS"
#echo $CMD
eval $CMD
