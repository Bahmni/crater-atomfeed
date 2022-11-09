#!/bin/sh
set -e

echo "Waiting for ${CRATER_ATOMFEED_DB_HOST}.."
sh wait-for.sh -t 300 "${CRATER_ATOMFEED_DB_HOST}":"${CRATER_ATOMFEED_DB_PORT}"

echo "Waiting for ${OPENMRS_HOST}.."
sh wait-for.sh -t 3600 "${OPENMRS_HOST}":"${OPENMRS_PORT}"

echo "[INFO] Starting Application"
java -jar crater-atomfeed.jar --crater.atomfeed.db.host=$CRATER_ATOMFEED_DB_HOST --crater.atomfeed.db.port=$CRATER_ATOMFEED_DB_PORT --crater.atomfeed.db.name=$CRATER_ATOMFEED_DB_NAME --crater.atomfeed.db.username=$CRATER_ATOMFEED_DB_USERNAME --crater.atomfeed.db.password=$CRATER_ATOMFEED_DB_PASSWORD --crater.url=$CRATER_URL --crater.username=$CRATER_USERNAME --crater.password=$CRATER_PASSWORD --openmrs.host=$OPENMRS_HOST --openmrs.port=$OPENMRS_PORT --openmrs.user=$OPENMRS_ATOMFEED_USER --openmrs.password=$OPENMRS_ATOMFEED_PASSWORD