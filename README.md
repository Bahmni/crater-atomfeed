# Crater Atomfeed

Crater is an open-source web & mobile app that helps you track expenses, payments & create professional invoices & estimates.
More information about Crater is provided on their [official website](https://craterapp.com/).

Bahmni uses Crater as a lightweight alternative to Odoo for invoicing & billing purposes, as a part of the Bahmni-Lite project. 
You can read more about it from this [presentation](https://bahmni.atlassian.net/l/cp/WkeRpuDc).

Crater-Atomfeed is an atomfeed client which serves as a way to link patients between Bahmni and Crater asynchronously.
OpenMRS acts as a publisher to publish all patients which are then created in Crater using REST API calls.

### Pre-requisite
* mvn 3.6.2 
* Java 1.8

The project requires 3 property files tp be kept at your home folder. Format of the property files will be as follows:

* application.properties

  * spring.datasource.url= 
  * spring.datasource.username=
  * spring.datasource.password=
  * spring.jpa.generate-ddl=
  * spring.jpa.database-platform=
  * spring.jpa.show-sql=
  * spring.jpa.hibernate.ddl-auto=
  * server.port=
  * liquibase.change-log=
  * enable.scheduling=


* atomfeed.properties

    * feed.author=
    * feed.connectionTimeoutInMilliseconds=
    * feed.replyTimeoutInMilliseconds=
    * feed.maxFailedEvents=
    * feed.failedEventMaxRetry=
    * openmrs.auth.uri=
    * openmrs.patient.feed.uri=
    * openmrs.user=
    * openmrs.password=
    * openmrs.connectionTimeoutInMilliseconds=
    * openmrs.replyTimeoutInMilliseconds=


* crater.properties

    * con.content_type =
    * con.accept = 
    * con.setDoOutput =
    * con.setDoInput =
    * crater.currencyid =
    * crater.url =
    * crater.company =
    * crater.device_name =
    * crater.username =
    * crater.password =


### Running the app
> cd /workspace-dir/crater-atomfeed/feed-integration-webapp/

> mvn clean install 

> mvn spring-boot:run

### Running Tests
> mvn clean test

### Documentation

Please take a look at [wiki](https://github.com/ICT4H/atomfeed/wiki) for documentation on how to use Atomfeed.

You can also go through the Bahmni [wiki](https://bahmni.atlassian.net/wiki/spaces/BAH/pages/3506200/Atom+Feed+Based+Synchronization+in+Bahmni) on details of the tables created and how Atomfeed works.

