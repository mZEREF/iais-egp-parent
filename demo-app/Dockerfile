FROM tomcat:9.0.36-jdk8-adoptopenjdk-hotspot

COPY target/demo.war /usr/local/tomcat/webapps/demo.war
COPY docker/setenv.sh /usr/local/tomcat/bin/setenv.sh
COPY docker/server.xml /usr/local/tomcat/conf/server.xml
COPY docker/run.sh /run.sh
COPY docker/admin.json /admin.json
COPY glowroot /glowroot

RUN chmod +x /usr/local/tomcat/bin/setenv.sh
RUN chmod +x /run.sh


