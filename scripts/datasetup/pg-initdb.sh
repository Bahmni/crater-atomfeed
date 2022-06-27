#!/bin/sh
# set -e
SCRIPTS_DIR=`dirname $0`
DATABASE_NAME="bahmni_feed_integration_example"
SERVICE_DB_SERVER="localhost"

# just an example to load up env vars to use in scripts
if [ -f /etc/your-service/svc.conf ]; then
. /etc/your-service/svc.conf
fi

if [ "$(psql -Upostgres -h $SERVICE_DB_SERVER -lqt | cut -d \| -f 1 | grep -w $DATABASE_NAME | wc -l)" -eq 0 ]; then
    echo "Creating database : $DATABASE_NAME"
    psql -U postgres -h $SERVICE_DB_SERVER -f $SCRIPTS_DIR/setupDB.sql
else
    echo "The database $DATABASE_NAME already exits"
fi