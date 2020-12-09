#!/bin/sh

JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom -Dlog4j.configurationFile=/etc/log4j2.xml"
#JAVA_OPTS="$JAVA_OPTS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000"