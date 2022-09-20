#!/bin/sh
set -e

echo "[INFO] Substituting Environment Variables"
envsubst < /opt/crater-atomfeed/etc/atomfeed.properties.template > ${HOME}/atomfeed.properties
envsubst < /opt/crater-atomfeed/etc/application.properties.template > ${HOME}/application.properties
envsubst < /opt/crater-atomfeed/etc/crater.properties.template > ${HOME}/crater.properties
envsubst < /opt/crater-atomfeed/etc/db.properties.template > ${HOME}/db.properties

echo "Waiting for ${CRATER_ATOMFEED_DB_HOST}.."
sh wait-for.sh -t 300 "${CRATER_ATOMFEED_DB_HOST}":"${CRATER_ATOMFEED_DB_PORT}"

echo "Waiting for ${OPENMRS_HOST}.."
sh wait-for.sh -t 3600 "${OPENMRS_HOST}":"${OPENMRS_PORT}"

echo "[INFO] Starting Application"
java -jar $SERVER_OPTS $DEBUG_OPTS /opt/crater-atomfeed/lib/crater-atomfeed.jar
