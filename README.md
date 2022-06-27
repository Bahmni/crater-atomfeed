# Crater Atomfeed


## Setup
To setup the example app's database, we will use hsqldb, file based and not in-memory.  
You may connect to any database you want and would need to configure properties and setup accordingly.

#### Pre-requisite
* mvn 3.6.2. 
* Java 1.8, works on 11 as well (with some warnings) 
* download HSQLDB from https://sourceforge.net/projects/hsqldb/files/latest/download (v 2.6.1) and unzip it to a folder.
The doc here assumes a directory "db" under the project directory

#### create db and start the database server
> cd /workspace-dir/crater-atomfeed/

> mkdir db

> cp /download-location/hsqldb-2.6.1.zip /workspace-dir/crater-atomfeed/db/

> cd db/

> unzip hsqldb-2.6.1.zip

> cd hsqldb-2.6.1/hsqldb/data

> java -cp ../lib/hsqldb-jdk8.jar org.hsqldb.server.Server --database.0 file:testdb --dbname.0 bahmni_feed_client_example


####  running the app 
> cd /workspace-dir/crater-atomfeed/feed-integration-webapp/

> cd feed-integration-webapp/

> mvn clean install 

> mvn spring-boot:run


 ####  test the app
 * open browser to "http://localhost:8080/health-check" to see "Hello World!"
