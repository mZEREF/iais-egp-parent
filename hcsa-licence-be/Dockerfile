FROM tomcat:9.0.36-jdk8-adoptopenjdk-hotspot

RUN cp -rp /usr/local/tomcat /tmp/tomcat && \
  rm -rf /usr/local/tomcat && \
  mv /tmp/tomcat /usr/local/tomcat && \
  rm -rf /usr/local/tomcat/webapps/docs /usr/local/tomcat/webapps/examples && \
  adduser -D -u 1000 iais && \
  chown -R iais:iais /usr/local/tomcat

ENV TZ=Asia/Singapore
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY setenv.sg.sh /usr/local/tomcat/bin/setenv.sh
COPY docker/server.xml /usr/local/tomcat/conf/server.xml
RUN chmod +x /usr/local/tomcat/bin/setenv.sh

COPY --chown=iais:iais target/hcsa-licence-web.war /usr/local/tomcat/webapps/hcsa-licence-web.war

COPY --chown=iais:iais docker/run.sh /run.sh
COPY --chown=iais:iais docker/admin.json /admin.json
COPY --chown=iais:iais docker/glowroot.jar /usr/local/tomcat/lib/glowroot.jar
COPY --chown=iais:iais docker/glowroot.properties /usr/local/tomcat/lib/glowroot.properties


RUN chmod a+x /run.sh

USER iais

CMD ["sh", "/run.sh"]
