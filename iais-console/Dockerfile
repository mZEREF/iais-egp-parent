FROM 192.168.7.88/library/tomcat:8.5.34-jre8-alpine

# Default to UTF-8 file.encoding
#ENV LANG C.UTF-8
#ENV NAS_SERVER_PATH https://nascloud
#ENV EGP_SERVER_PATH https://egpcloud

ADD target/console /usr/local/tomcat/webapps/console
# COPY docker/hosts /hosts
COPY docker/console.xml /usr/local/tomcat/conf/Catalina/localhost/console.xml
COPY docker/mssql-jdbc-6.4.0.jre8.jar /usr/local/tomcat/lib/mssql-jdbc-6.4.0.jre8.jar
COPY docker/setenv.sh /usr/local/tomcat/bin/setenv.sh
COPY docker/run.sh /run.sh
COPY docker/libsigar-amd64-linux.so /usr/local/tomcat/native-jni-lib

COPY docker/server.xml /usr/local/tomcat/conf/server.xml

RUN apk add --update font-adobe-100dpi ttf-dejavu fontconfig
RUN chmod +x /usr/local/tomcat/bin/setenv.sh \
    && chmod +x /run.sh

CMD ["sh", "/run.sh"]
