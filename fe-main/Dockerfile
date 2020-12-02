FROM tomcat:9.0.36-jdk8-adoptopenjdk-hotspot

RUN useradd -ms /bin/bash iais

WORKDIR /home/iais

ENV TZ=Asia/Singapore
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
RUN chmod -R  o+w /usr/local/tomcat/webapps
COPY target/main-web.war /usr/local/tomcat/webapps/main-web.war

COPY docker/setenv.sh /usr/local/tomcat/bin/setenv.sh
COPY docker/server.xml /usr/local/tomcat/conf/server.xml
COPY docker/run.sh /run.sh
COPY docker/admin.json /admin.json
COPY glowroot /glowroot

RUN chmod +x /usr/local/tomcat/bin/setenv.sh
RUN chmod +x /run.sh

USER iais

CMD ["sh", "/run.sh"]