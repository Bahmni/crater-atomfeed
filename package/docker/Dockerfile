FROM openjdk:8-alpine
ENV SERVER_PORT=8080
ENV BASE_DIR=/var/run/crater-atomfeed
ENV CONTEXT_PATH=/crater-atomfeed
ENV WAR_DIRECTORY=/var/run/crater-atomfeed/crater-atomfeed
ENV SERVER_OPTS="-Xms512m -Xmx1024m -XX:PermSize=256m -XX:MaxPermSize=512m"
ENV DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,address=8002,server=y,suspend=n"

# Used by envsubst command for replacing environment values at runtime
RUN apk add gettext curl
ADD https://repo.mybahmni.org/packages/build/bahmni-embedded-tomcat-8.0.42.jar /opt/crater-atomfeed/lib/crater-atomfeed.jar
COPY feed-integration-webapp/target/crater-atomfeed.war /etc/crater-atomfeed/crater-atomfeed.war
RUN mkdir -p ${WAR_DIRECTORY}
RUN cd ${WAR_DIRECTORY} && jar xvf /etc/crater-atomfeed/crater-atomfeed.war

COPY package/docker/templates/atomfeed.properties.template /opt/crater-atomfeed/etc/atomfeed.properties.template
COPY package/docker/templates/application.properties.template /opt/crater-atomfeed/etc/application.properties.template
COPY package/docker/templates/crater.properties.template /opt/crater-atomfeed/etc/crater.properties.template
RUN curl -o wait-for.sh 'https://raw.githubusercontent.com/eficode/wait-for/v2.2.3/wait-for'
COPY package/docker/scripts/start.sh start.sh
RUN chmod +x start.sh 
RUN chmod +x wait-for.sh
CMD [ "./start.sh" ]
