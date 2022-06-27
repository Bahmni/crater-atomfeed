FROM openjdk:8-alpine

ENV SERVER_PORT=8053
ENV BASE_DIR=/var/run/crater-atomfeed
ENV CONTEXT_PATH=/crater-atomfeed
ENV WAR_DIRECTORY=/var/run/crater-atomfeed/crater-atomfeed
ENV SERVER_OPTS="-Xms512m -Xmx1024m -XX:PermSize=256m -XX:MaxPermSize=512m"
ENV DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,address=8002,server=y,suspend=n"

# Used by envsubst command for replacing environment values at runtime
# RUN apk add gettext

# start here

# bahmni-core.jar contains embedded tomcat server and is built from bahmni-package/core directory
# COPY package/resources/core-1.0-SNAPSHOT.jar /opt/crater-atomfeed/lib/bahmni-erp-connect.jar
# COPY openerp-atomfeed-service/target/openerp-atomfeed-service.war /etc/crater-atomfeed/openerp-atomfeed-service.war

# RUN mkdir -p ${WAR_DIRECTORY}
# RUN cd ${WAR_DIRECTORY} && jar xvf /etc/crater-atomfeed/openerp-atomfeed-service.war

# COPY resources/log4j2.xml ${WAR_DIRECTORY}/WEB-INF/classes/
# COPY docker/templates/atomfeed.properties.template /opt/crater-atomfeed/etc/atomfeed.properties.template
# COPY package/docker/scripts/run-liquibase.sh /opt/crater-atomfeed/etc/run-liquibase.sh
# RUN chmod +x /opt/crater-atomfeed/etc/run-liquibase.sh

# COPY package/docker/scripts/start.sh start.sh
# RUN chmod +x start.sh
# CMD [ "./start.sh" ]


ARG JAR_FILE=./crater-atomfeed/crater-atomfeed.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
