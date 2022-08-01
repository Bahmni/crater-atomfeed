FROM openjdk:8-alpine

ENV SERVER_PORT=8054
ENV BASE_DIR=/var/run/crater
ENV CONTEXT_PATH=/crater
ENV WAR_DIRECTORY=/var/run/crater/crater
ENV SERVER_OPTS="-Xms512m -Xmx1024m -XX:PermSize=256m -XX:MaxPermSize=512m"
ENV DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=n"

# Used by envsubst command for replacing environment values at runtime
RUN apk add gettext curl mysql-client

COPY COPY ../feed-integration-webapp/target/crater-atomfeed.jar /opt/crater/lib/crater-atomfeed.jar
COPY ../feed-integration-webapp/target/crater-atomfeed.jar /etc/crater/crater.war

RUN mkdir -p ${WAR_DIRECTORY}
RUN cd ${WAR_DIRECTORY} && jar xvf /etc/crater/crater.war

COPY package/docker/templates/atomfeed.properties.template /etc/atomfeed.properties.template
COPY package/docker/templates/application.properties.template /etc/application.properties.template
RUN curl -o wait-for.sh 'https://raw.githubusercontent.com/eficode/wait-for/v2.2.3/wait-for'

COPY package/docker/scripts/start.sh start.sh
RUN chmod +x start.sh
RUN chmod +x wait-for.sh

CMD [ "./start.sh" ]

# FROM openjdk:8-alpine
# COPY ../feed-integration-webapp/target/crater-atomfeed.jar .
# ARG DEBIAN_FRONTEND=noninteractive
# RUN apk update
# RUN apk add mysql-client
# COPY package/docker/templates/atomfeed.properties.template /etc/atomfeed.properties.template
# COPY package/docker/templates/application.properties.template /etc/application.properties.template
# RUN curl -o wait-for.sh 'https://raw.githubusercontent.com/eficode/wait-for/v2.2.3/wait-for'
# COPY package/docker/scripts/start.sh start.sh
# RUN chmod +x start.sh
# RUN chmod +x wait-for.sh
# CMD [ "./start.sh" ]
# # CMD ["java","-jar","/crater-atomfeed.jar"]