# Crater Atomfeed

Crater Atomfeed is an atomfeed client designed to facilitate asynchronous patient synchronization between Bahmni and Crater, utilizing OpenMRS as the publisher. This integration serves as a lightweight alternative to Odoo for invoicing and billing within the Bahmni-Lite project.

## About Crater

Crater is an open-source application that enables users to manage expenses, payments, and create professional invoices and estimates. More information about Crater can be found on the [official website](https://craterapp.com/).

## Bahmni Integration

Bahmni leverages Crater as part of its Bahmni-Lite initiative, integrating it for invoicing and billing functionalities. Further details are available in this [presentation](https://bahmni.atlassian.net/l/cp/WkeRpuDc).

## Prerequisites

Ensure the following tools are installed:

- Apache Maven 3.6.2
- Java 11

## Running the Application

To build and run Crater-Atomfeed:

```bash
cd /workspace-dir/crater-atomfeed/feed-integration-webapp/
mvn clean install
mvn spring-boot:run
```

## Running Tests

Execute the following command to run tests:

```bash
mvn clean test
```

## Documentation

Refer to the following resources for documentation:

- [Atomfeed Wiki](https://github.com/ICT4H/atomfeed/wiki): Provides details on using Atomfeed.
- [Bahmni Wiki](https://bahmni.atlassian.net/wiki/spaces/BAH/pages/3506200/Atom+Feed+Based+Synchronization+in+Bahmni): Explains the tables created and operational details of Atomfeed in Bahmni.
