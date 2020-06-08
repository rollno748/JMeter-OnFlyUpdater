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
* Once the test started, the Spark server will start a REST server according to the config provided. The default will be http://127.0.0.1:1304/on-fly/
* The Rest services supported are as follows. e.g; http://localhost:1304/on-fly/ping
* The On-Fly Updated will have a credentials based control to REST services, it requires a header password to be send along with the REST services.

## Supported REST Services

|Service|HTTP Method|URI|QueryParams|ReqBody|Status|
|:---|:---:|:---|:---|:---|:---:|
PluginRunningStatus|GET|/{URI-PATH}/ping|NA||Completed
PropertyDisplay|GET|/{URI-PATH}/properties?type={type}|jmeter/system||Completed
GetStatus|GET|/{URI-PATH}/status|NA||Completed
TestInfo|GET|/{URI-PATH}/testinfo|NA||In Progress
JmeterVariables|GET|/{URI-PATH}/vars|NA||In Progress
UpdateThreads|PUT|/{URI-PATH}/threads|NA||In Progress
UpdateProperties|PUT|/{URI-PATH}/properties|NA||Completed
UpdateJmeterVariables|PUT|/{URI-PATH}/vars|NA||In Progress
UpdateThreadGroups|PUT|/{URI-PATH}/threadgroups|NA||In Progress
UpdateTestElement|PUT|/{URI-PATH}/element|NA||In Progress
StopTest|POST|/{URI-PATH}/stoptest?action={action}|shutdown/stop||Completed

## Additional Info
Some more info on the plugin

* The REST services will be active only during the test. The End of test will terminate the REST services
* Plugin Running status also initialised during the test
* Observed some abnormal behavior on the plugin when the test is restarted without killing the JMeter JVM


## References
* Plugin's Core Idea: https://octoperf.com/blog/2020/03/15/beanshell-server
* REST servies: http://sparkjava.com/documentation
* Examples: https://www.baeldung.com/spark-framework-rest-api

## Tools Used
* PostMan
* Eclipse
* Markdown editor online (https://dillinger.io/)

