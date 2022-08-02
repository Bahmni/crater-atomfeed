FROM tomcat:latest
# Crater steps
RUN apt-get update -y
RUN apt-get install -y gettext
WORKDIR /usr/local/tomcat
RUN mkdir -p war
COPY ../feed-integration-webapp/target/crater-atomfeed.war war/crater-atomfeed.war
WORKDIR /usr/local/tomcat/war
RUN jar xvf crater-atomfeed.war
COPY package/docker/templates/atomfeed.properties.template atomfeed.properties.template
COPY package/docker/templates/application.properties.template application.properties.template
COPY package/docker/templates/crater.properties.template crater.properties.template
RUN curl -o wait-for.sh 'https://raw.githubusercontent.com/eficode/wait-for/v2.2.3/wait-for'
COPY package/docker/scripts/start.sh start.sh
RUN chmod +x start.sh
RUN chmod +x wait-for.sh
CMD [ "./start.sh" ]
