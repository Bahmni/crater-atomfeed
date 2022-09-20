#!/bin/sh
set -e

echo "[INFO] Substituting Environment Variables"
envsubst < /opt/crater-atomfeed/etc/atomfeed.properties.template > ${WAR_DIRECTORY}/WEB-INF/classes/atomfeed.properties
envsubst < /opt/crater-atomfeed/etc/application.properties.template > ${WAR_DIRECTORY}/WEB-INF/classes/application.properties
envsubst < /opt/crater-atomfeed/etc/crater.properties.template > ${WAR_DIRECTORY}/WEB-INF/classes/crater.properties

echo "Waiting for ${CRATER_ATOMFEED_DB_HOST}.."
sh wait-for.sh -t 300 "${CRATER_ATOMFEED_DB_HOST}":"${CRATER_ATOMFEED_DB_PORT}"

echo "Waiting for ${OPENMRS_HOST}.."
sh wait-for.sh -t 3600 "${OPENMRS_HOST}":"${OPENMRS_PORT}"

echo "[INFO] Starting Application"
java -jar $SERVER_OPTS $DEBUG_OPTS /opt/crater-atomfeed/lib/crater-atomfeed.jar
