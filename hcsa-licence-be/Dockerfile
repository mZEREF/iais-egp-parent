FROM tomcat:9.0.31-jdk8-adoptopenjdk-hotspot

ENV TZ=Asia/Singapore
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY target/hcsa-licence-web.war /usr/local/tomcat/webapps/hcsa-licence-web.war

COPY docker/setenv.sh /usr/local/tomcat/bin/setenv.sh
COPY docker/server.xml /usr/local/tomcat/conf/server.xml
COPY docker/run.sh /run.sh
COPY docker/admin.json /admin.json
COPY glowroot /glowroot

RUN chmod +x /usr/local/tomcat/bin/setenv.sh
RUN chmod +x /run.sh

RUN cp -rp /usr/local/tomcat /usr/local/tomcat-bak && \
    rm -rf /usr/local/tomcat && \
    mv /usr/local/tomcat-bak /usr/local/tomcat && \
    adduser -D -u 1000 iais && \
    chown -R iais:iais /usr/local/tomcat

USER iais
