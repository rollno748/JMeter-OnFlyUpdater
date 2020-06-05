# Jmeter-OnFlyUpdater

## Introduction
Jmeter plugin to get/add/update properties, variables, Threads during the run using APIs

This plugin adds feature to control jmeter over REST APIs


## Required Components

1. Jmeter


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
* Jar will be generated under the target directory (jmeter-pubsub-sampler-1.0.jar)
* Copy the Jar to \<Jmeter Installed Directory\>/lib/ext/

## How to use it
Add required config element (On-Fly-Updater config)

* Set a password for authentication (Default password will be Upd@t3M3)
* Set the Port number on which the Spark Services to run (Optional, Defaults to 1304)
* Set the URI path (Optional, Defaults to /on-fly)


## REST Services
* Check the API Status (GET http://localhost:1304/on-fly/status)
* Get the JMeter Properties (GET http://localhost:1304/on-fly/props?type=system/jmeter)
* Get the JMeter variables (GET http://localhost:1304/on-fly/jmetervars)
* Get the thread Information (GET http://localhost:1304/on-fly/threadinfo) 
* Stop the JMeter test (GET http://localhost:1304/on-fly/stoptest)
* Update the Properties/Variables (PUT http://localhost:1304/on-fly/update)

## Features
- [x] GET Plugin Running Status
- [x] GET Property Display (System/Jmeter)
- [x] GET Status (Overall User Running Status)
- [ ] GET Test Info
- [ ] GET Jmeter Variables
- [ ] PUT Threads (Add/Remove)
- [ ] PUT Properties (Supports only Jmeter properties) 
- [ ] PUT Jmeter Variables
- [ ] PUT Thread Groups (Enable/Disable)
- [ ] PUT Element (Enable/Disable Listeners)
- [ ] POST Stop test (Stop/Shutdown)

##References
* REST servies: http://sparkjava.com/documentation
* Examples: https://www.baeldung.com/spark-framework-rest-api


