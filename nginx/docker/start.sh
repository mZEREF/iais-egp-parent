#!/bin/bash

filebeat -e -c /etc/filebeat/filebeat.yml &
/usr/sbin/nginx
tail -f /var/log/nginx/access.log -f /var/log/nginx/error.log
