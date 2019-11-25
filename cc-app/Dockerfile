FROM 192.168.7.88/library/tomcat:8.5.34-jre8-alpine

COPY target/hcsaapplication.war /usr/local/tomcat/webapps/hcsaapplication.war

COPY docker/setenv.sh /usr/local/tomcat/bin/setenv.sh
COPY docker/server.xml /usr/local/tomcat/conf/server.xml
COPY docker/run.sh /run.sh
COPY docker/admin.json /admin.json
COPY glowroot /glowroot

RUN chmod +x /usr/local/tomcat/bin/setenv.sh
RUN chmod +x /run.sh



