#!/bin/bash
sed -i 's?https://nascloud?'"$NAS_SERVER_PATH"'?g' /usr/local/tomcat/webapps/console/WEB-INF/classes/sqlscripts/sso_sqlserver.sql
sed -i 's?https://egpcloud?'"$EGP_SERVER_PATH"'?g' /usr/local/tomcat/webapps/console/WEB-INF/classes/sqlscripts/sso_sqlserver.sql
sed -i 's?sqlserver_path?'"$SQLSERVER_PATH"'?g' /usr/local/tomcat/conf/Catalina/localhost/console.xml
sed -i 's?sqlserver_port?'"$SQLSERVER_PORT"'?g' /usr/local/tomcat/conf/Catalina/localhost/console.xml
sed -i 's?sqlserver_user?'"$SQLSERVER_USER"'?g' /usr/local/tomcat/conf/Catalina/localhost/console.xml
sed -i 's?sqlserver_password?'"$SQLSERVER_PASSWORD"'?g' /usr/local/tomcat/conf/Catalina/localhost/console.xml
sed -i 's?sqlserver_db?'"$SQLSERVER_DB"'?g' /usr/local/tomcat/conf/Catalina/localhost/console.xml
catalina.sh run