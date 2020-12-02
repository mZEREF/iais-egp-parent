FROM tomcat:9.0.36-jdk8-adoptopenjdk-hotspot

RUN useradd -ms /bin/bash iais

WORKDIR /home/iais

ENV TZ=Asia/Singapore
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
RUN chown -R iais:iais /usr/local/tomcat
COPY --chown=iais:iais target/main-web.war /usr/local/tomcat/webapps/main-web.war

COPY --chown=iais:iais docker/setenv.sh /usr/local/tomcat/bin/setenv.sh
COPY --chown=iais:iais docker/server.xml /usr/local/tomcat/conf/server.xml
COPY --chown=iais:iais docker/run.sh /run.sh
COPY --chown=iais:iais docker/admin.json /admin.json
COPY --chown=iais:iais glowroot /glowroot

RUN chmod +x /usr/local/tomcat/bin/setenv.sh
RUN chmod +x /run.sh

USER iais

CMD ["sh", "/run.sh"]