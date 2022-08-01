FROM openjdk:8-alpine
COPY ../feed-integration-webapp/target/crater-atomfeed.jar .
ARG DEBIAN_FRONTEND=noninteractive
RUN apk update
RUN apk add mysql-client
COPY package/docker/templates/atomfeed.properties.template /etc/atomfeed.properties.template
COPY package/docker/templates/application.properties.template /etc/application.properties.template
RUN curl -o wait-for.sh 'https://raw.githubusercontent.com/eficode/wait-for/v2.2.3/wait-for'
COPY package/docker/scripts/start.sh start.sh
RUN chmod +x start.sh
RUN chmod +x wait-for.sh
CMD [ "./start.sh" ]
# CMD ["java","-jar","/crater-atomfeed.jar"]