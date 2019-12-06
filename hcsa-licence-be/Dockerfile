FROM 192.168.7.88/ecq/tomcat:9.0.21-adoptopenjdk8-alpine3.10

COPY target/hcsa-licence.war /usr/local/tomcat/webapps/hcsa-licence.war

COPY docker/setenv.sh /usr/local/tomcat/bin/setenv.sh
COPY docker/server.xml /usr/local/tomcat/conf/server.xml
COPY docker/run.sh /run.sh
COPY docker/admin.json /admin.json
COPY glowroot /glowroot

RUN chmod +x /usr/local/tomcat/bin/setenv.sh
RUN chmod +x /run.sh



