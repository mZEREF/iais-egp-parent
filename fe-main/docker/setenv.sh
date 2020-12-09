#!/bin/sh

JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom"
#JAVA_OPTS="$JAVA_OPTS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000"