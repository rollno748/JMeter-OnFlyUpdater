# Jmeter-OnFlyUpdater

## Introduction
Jmeter plugin to get/add/update properties, variables, Threads during the run using APIs

This plugin adds feature to control jmeter over REST APIs


## Required Components

1. Jmeter
2. Postman/cURL

## Jar Dependencies Required

* spark-core-2.8.0.jar
* javax.servlet-api-3.1.0.jar
* jetty-server-9.4.12.v20180830.jar
* jetty-util-9.4.12.v20180830.jar
* jetty-http-9.4.12.v20180830.jar
* jetty-io-9.4.12.v20180830.jar
* gson-2.2.4.jar


## Jmeter Target

* Jmeter version 5.1.1 or above
* Java 8 or above


## Installation Instructions

* Download the source code from the Github.
* Just do a mvn clean install (Git bash is required)
* Jar will be generated under the target directory (jmeter-onfly-updater-0.1.jar)
* Copy the Jar to \<Jmeter Installed Directory\>/lib/ext/

## How to use it
Add required config element (On-Fly-Updater config)

* Set a password for authentication (Default password will be Upd@t3M3)
* Set the Port number on which the Spark Services to run (Optional, Defaults to 1304)
* Set the URI path (Optional, Defaults to /on-fly)


## Supported REST Services
- [x] GET Plugin Running Status (GET http://{IP/Hostname}:{Port}/{URI-PATH}/ping)
- [x] GET Property Display (System/Jmeter) (GET http://{IP/Hostname}:{Port}/{URI-PATH}/properties?type=jmeter)
- [x] GET Status (Overall User Running Status) (GET http://{IP/Hostname}:{Port}/{URI-PATH}/status)
- [ ] GET Test Info (GET http://{IP/Hostname}:{Port}/{URI-PATH}/testinfo)
- [ ] GET Jmeter Variables (GET http://{IP/Hostname}:{Port}/{URI-PATH}/vars)
- [ ] PUT Threads (Add/Remove) (GET http://{IP/Hostname}:{Port}/{URI-PATH}/threads)
- [ ] PUT Properties (Supports only Jmeter properties) (GET http://{IP/Hostname}:{Port}/{URI-PATH}/properties)
- [ ] PUT Jmeter Variables (GET http://{IP/Hostname}:{Port}/{URI-PATH}/vars)
- [ ] PUT Thread Groups (Enable/Disable) (GET http://{IP/Hostname}:{Port}/{URI-PATH}/threadgroups)
- [ ] PUT Element (Enable/Disable Listeners) (GET http://{IP/Hostname}:{Port}/{URI-PATH}/element)
- [x] POST Stop test (Stop/Shutdown) (GET http://{IP/Hostname}:{Port}/{URI-PATH}/stoptest?action=shutdown)

## References
* REST servies: http://sparkjava.com/documentation
* Examples: https://www.baeldung.com/spark-framework-rest-api


