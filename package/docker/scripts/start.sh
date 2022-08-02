#!/bin/sh
set -e

echo "[INFO] Substituting Environment Variables"
envsubst < /$CATALINA_HOME/atomfeed.properties.template > $CATALINA_HOME/WEB-INF/classes/atomfeed.properties
envsubst < /$CATALINA_HOME/application.properties.template > /$CATALINA_HOME/WEB-INF/classes/application.properties
envsubst < /$CATALINA_HOME/crater.properties.template > /$CATALINA_HOME/WEB-INF/classes/crater.properties
echo "-----contents of atomfeed.properties-----"
cat $CATALINA_HOME/WEB-INF/classes/atomfeed.properties
echo "-----contents of application.properties-----"
cat $CATALINA_HOME/WEB-INF/classes/application.properties
echo "-----contents of crater.properties------"
cat "$CATALINA_HOME/WEB-INF/classes/crater.properties"
echo "---------------------------------------------"
echo "Waiting for ${CRATER_DB_HOST}.."
sh wait-for.sh -t 300 "${CRATER_DB_HOST}":"${CRATER_DB_PORT}"

echo "Waiting for ${OPENMRS_HOST}.."
sh wait-for.sh -t 300 "${OPENMRS_HOST}":"${OPENMRS_PORT}"

echo "[INFO] Starting Application"
pwd
ls
ls WEB-INF
# java -jar $SERVER_OPTS $DEBUG_OPTS /$CATALINA_HOME/crater.$CATALINA_HOME
$CATALINA_HOME/bin/catalina.sh run