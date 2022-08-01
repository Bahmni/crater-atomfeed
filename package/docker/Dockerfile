FROM openjdk:8-alpine
COPY ../feed-integration-webapp/target/crater-atomfeed.jar .
ARG DEBIAN_FRONTEND=noninteractive
RUN apk update
RUN apk add mysql-client
CMD ["java","-jar","/crater-atomfeed.jar"]