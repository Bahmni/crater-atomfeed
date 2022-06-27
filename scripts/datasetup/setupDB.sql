CREATE USER examplefeeduser WITH PASSWORD 'password';
CREATE DATABASE bahmni_feed_integration_example;
GRANT ALL PRIVILEGES ON database bahmni_feed_integration_example to examplefeeduser;
