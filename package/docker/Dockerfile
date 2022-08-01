FROM openjdk:8-alpine
RUN mkdir war
RUN apk add curl gettext
COPY ../feed-integration-webapp/target/crater-atomfeed.war /war/crater-atomfeed.war
RUN cd war && jar xvf /war/crater-atomfeed.war
COPY package/docker/templates/atomfeed.properties.template /war/atomfeed.properties.template
COPY package/docker/templates/application.properties.template /war/application.properties.template
COPY package/docker/templates/crater.properties.template /war/crater.properties.template
RUN curl -o wait-for.sh 'https://raw.githubusercontent.com/eficode/wait-for/v2.2.3/wait-for'
COPY package/docker/scripts/start.sh start.sh
RUN chmod +x start.sh
RUN chmod +x wait-for.sh
CMD [ "./start.sh" ]
# CMD ["java","-jar","/crater-atomfeed.jar"]