FROM 192.168.7.88/library/tomcat:8.5.34-jre8-alpine

COPY target/web.war /usr/local/tomcat/webapps/web.war

COPY docker/setenv.sh /usr/local/tomcat/bin/setenv.sh
COPY docker/server.xml /usr/local/tomcat/conf/server.xml
RUN chmod +x /usr/local/tomcat/bin/setenv.sh

CMD ["/usr/local/tomcat/bin/catalina.sh", "run"]



