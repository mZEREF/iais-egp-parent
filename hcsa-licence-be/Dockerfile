FROM tomcat:9.0.36-jdk8-adoptopenjdk-hotspot

RUN useradd -ms /bin/bash iais
RUN chown -R iais:iais /usr/local/tomcat

ENV TZ=Asia/Singapore
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY setenv.sg.sh /usr/local/tomcat/bin/setenv.sh
COPY docker/server.xml /usr/local/tomcat/conf/server.xml
RUN chmod +x /usr/local/tomcat/bin/setenv.sh

COPY --chown=iais:iais target/hcsa-licence-web.war /usr/local/tomcat/webapps/hcsa-licence-web.war

COPY --chown=iais:iais docker/run.sh /run.sh
COPY --chown=iais:iais docker/admin.json /admin.json
COPY --chown=iais:iais glowroot /glowroot


RUN chmod a+x /run.sh

USER iais

CMD ["sh", "/run.sh"]
