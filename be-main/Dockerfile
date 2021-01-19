FROM 192.168.7.88/library/tomcat:9.0.38-jre8-alpine

RUN deluser --remove-home egpcloud
RUN adduser -D -u 1000 iais
RUN chown -R iais:iais /usr/local/tomcat

ENV TZ=Asia/Singapore
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY docker/setenv.sh /usr/local/tomcat/bin/setenv.sh
COPY docker/server.xml /usr/local/tomcat/conf/server.xml
RUN chmod +x /usr/local/tomcat/bin/setenv.sh

COPY --chown=iais:iais target/main-web /usr/local/tomcat/webapps/main-web

COPY --chown=iais:iais docker/run.sh /run.sh
COPY --chown=iais:iais docker/admin.json /admin.json
COPY --chown=iais:iais glowroot /glowroot


RUN chmod a+x /run.sh

USER iais

CMD ["sh", "/run.sh"]

