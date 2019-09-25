#!/bin/sh

# test if container running with elk-filebeat

if [ -f /etc/filebeat/filebeat.yml ]; then
  filebeat -e -c /etc/filebeat/filebeat.yml &
fi

catalina.sh run

