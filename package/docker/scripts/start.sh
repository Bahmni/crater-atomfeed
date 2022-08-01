#!/bin/sh
set -e

echo "[INFO] Substituting Environment Variables"
envsubst < /war/atomfeed.properties.template > /war/WEB-INF/classes/atomfeed.properties
envsubst < /war/application.properties.template > /war/WEB-INF/classes/application.properties
envsubst < /war/crater.properties.template > /war/WEB-INF/classes/crater.properties

echo "Waiting for ${DB_HOST}.."
sh wait-for.sh -t 300 "${DB_HOST}":"${DB_PORT}"

echo "Waiting for ${OPENMRS_HOST}.."
sh wait-for.sh -t 300 "${OPENMRS_HOST}":"${OPENMRS_PORT}"

echo "[INFO] Starting Application"
java -jar $SERVER_OPTS $DEBUG_OPTS /opt/crater/lib/crater.jar