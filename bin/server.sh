#!/bin/bash

WORKING_DIR=`cd $(dirname $0); pwd`
TARGET_DIR=`cd ${WORKING_DIR}/../target; pwd`

CLASS="eu.cloudtm.ServerMain"

#fenix framework
D_VARS="-Dfenixframework.appName=server"
D_VARS="$D_VARS -Dfenixframework.domainModelURLs=fenix-framework-domain-root.dml,books.dml"
D_VARS="$D_VARS -Dfenixframework.ispnConfigFile=ispn.xml"
D_VARS="$D_VARS -Dfenixframework.coreThreadPoolSize=1"
D_VARS="$D_VARS -Dfenixframework.maxThreadPoolSize=2"
D_VARS="$D_VARS -Dfenixframework.keepAliveTime=60000"
D_VARS="$D_VARS -Dfenixframework.messagingJgroupsFile=jgroups.xml"

#jgroups IPv4
D_VARS="$D_VARS -Djava.net.preferIPv4Stack=true -Djgroups.bind_addr=127.0.0.1"

#log4j
D_VARS="$D_VARS -Dlog4j.configuration=file:$WORKING_DIR/log4j.properties"

#class path
CP="$TARGET_DIR/fenix-framework-app-1.0.jar"

for jar in `ls ${TARGET_DIR}/dependency/*.jar`; do
    CP="$CP:$jar";
done

CMD="java $D_VARS -cp $CP $CLASS"
echo $CMD
eval $CMD
