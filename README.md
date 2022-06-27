# bahmni feed integration example


## Setup
To setup the example app's database, we will use hsqldb, file based and not in-memory.  
You may connect to any database you want and would need to configure properties and setup accordingly.

#### Pre-requisite
* mvn 3.6.2. 
* Java 1.8, works on 11 as well (with some warnings) 
* download HSQLDB from https://sourceforge.net/projects/hsqldb/files/latest/download (v 2.6.1) and unzip it to a folder.
The doc here assumes a directory "db" under the project directory

#### create db and start the database server
> cd /workspace-dir/bahmni-feed-integration-example/

> mkdir db

> cp /download-location/hsqldb-2.6.1.zip /workspace-dir/bahmni-feed-integration-example/db/

> cd db/

> unzip hsqldb-2.6.1.zip

> cd hsqldb-2.6.1/hsqldb/data

> java -cp ../lib/hsqldb-jdk8.jar org.hsqldb.server.Server --database.0 file:testdb --dbname.0 bahmni_feed_client_example


####  running the app 
> cd /workspace-dir/bahmni-feed-integration-example/feed-integration-webapp/

> cd feed-integration-webapp/

> mvn clean install 

> mvn spring-boot:run

####  check with the hsql db client 
> cd /workspace-dir/bahmni-feed-integration-example/db/hsqldb-2.6.1/hsqldb/data
 
> java -cp ../lib/hsqldb-jdk8.jar org.hsqldb.util.DatabaseManager

* The above show a rudimentary UI, specify
- type: HSQL Database engine server 
- url: jdbc:hsqldb:hsql://localhost/bahmni_feed_client_example
- user: SA
- password: 

* Clicking ok, and on left panel you should be able to see tables like - failed_events, markers, etc. 
 
 
 ####  test the app
 * open browser to "http://localhost:8080/health-check" to see "Hello World!"
 
 
 ### useful info
 * Change log level in the log4j2.xml, the log is output at /tmp/bahmni-feed-integration-example.log
 * Atomfeed set the markers to first page if you don't set it. 
 So, Set the markers manually after provisioning and before deployment. Especially openmrs patient/encounter feed as we are reading patient/encounter feed to figure out the appropriate actions.
 
 * Use the following sql query to set the markers manually according to the events in your machine. 
 (change the last_read_entry_id and feed_uri_for_last_read_entry )
 
 > insert into markers (feed_uri, last_read_entry_id, feed_uri_for_last_read_entry) 
     values ('http://loalhost:8080/openmrs/ws/atomfeed/encounter/recent', '?', '?');
 
 ### TODO
 1. Upgrade spring 4 to latest. Right now running on Java 11, gives following error
 >  WARNING: An illegal reflective access operation has occurred
    WARNING: Illegal reflective access by org.springframework.cglib.core.ReflectUtils$2 (file:/Users/angshus/.m2/repository/org/springframework/spring-core/4.1.6.RELEASE/spring-core-4.1.6.RELEASE.jar) to method java.lang.ClassLoader.defineClass(java.lang.String,byte[],int,int,java.security.ProtectionDomain)
    WARNING: Please consider reporting this to the maintainers of org.springframework.cglib.core.ReflectUtils$2
    WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
    WARNING: All illegal access operations will be denied in a future release 
