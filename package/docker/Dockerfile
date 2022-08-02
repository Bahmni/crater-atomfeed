FROM openjdk:8-alpine
ENV TOMCAT_MAJOR=8
ENV TOMCAT_VERSION=8.5.37
ENV CATALINA_HOME=/opt/tomcat
RUN apk add curl gettext ttf-dejavu
RUN mkdir -p /opt
# install tomcat
RUN curl -jkSL -o /tmp/apache-tomcat.tar.gz http://archive.apache.org/dist/tomcat/tomcat-${TOMCAT_MAJOR}/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz && \
    gunzip /tmp/apache-tomcat.tar.gz && \
    tar -C /opt -xf /tmp/apache-tomcat.tar && \
    ln -s /opt/apache-tomcat-$TOMCAT_VERSION $CATALINA_HOME
    
EXPOSE 8080

WORKDIR $CATALINA_HOME
# Crater steps
COPY ../feed-integration-webapp/target/crater-atomfeed.war crater-atomfeed.war
RUN jar xvf crater-atomfeed.war
COPY package/docker/templates/atomfeed.properties.template atomfeed.properties.template
COPY package/docker/templates/application.properties.template application.properties.template
COPY package/docker/templates/crater.properties.template crater.properties.template
RUN curl -o wait-for.sh 'https://raw.githubusercontent.com/eficode/wait-for/v2.2.3/wait-for'
COPY package/docker/scripts/start.sh start.sh
RUN chmod +x start.sh
RUN chmod +x wait-for.sh
CMD [ "./start.sh" ]
