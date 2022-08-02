#!/bin/sh
set -e

echo "[INFO] Substituting Environment Variables"
envsubst < /usr/local/tomcat/war/atomfeed.properties.template > /usr/local/tomcat/war/WEB-INF/classes/atomfeed.properties
envsubst < /usr/local/tomcat/war/application.properties.template > /usr/local/tomcat/war/WEB-INF/classes/application.properties
envsubst < /usr/local/tomcat/war/crater.properties.template > /usr/local/tomcat/war/WEB-INF/classes/crater.properties
echo "-----contents of atomfeed.properties-----"
cat /usr/local/tomcat/war/WEB-INF/classes/atomfeed.properties
echo "-----contents of application.properties-----"
cat /usr/local/tomcat/war/WEB-INF/classes/application.properties
echo "-----contents of crater.properties------"
cat /usr/local/tomcat/war/WEB-INF/classes/crater.properties
echo "---------------------------------------------"
echo "Waiting for ${CRATER_DB_HOST}.."
sh wait-for.sh -t 300 "${CRATER_DB_HOST}":"${CRATER_DB_PORT}"

echo "Waiting for ${OPENMRS_HOST}.."
sh wait-for.sh -t 300 "${OPENMRS_HOST}":"${OPENMRS_PORT}"

# rm crater-atomfeed.war
# jar cvf crater-atomfeed.war *
# mv crater-atomfeed.war webapps
echo "[INFO] Starting Application"
# java -jar $SERVER_OPTS $DEBUG_OPTS /usr/local/tomcat/crater.usr/local/tomcat
usr/local/tomcat/bin/catalina.sh run