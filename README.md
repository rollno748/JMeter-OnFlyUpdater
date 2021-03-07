# Jmeter-OnFlyUpdater

## Introduction
Jmeter plugin to control Jmeter during the running status. This plugin adds feature to control jmeter over REST APIs

## Required Components

1. Jmeter
2. Postman/cURL

## Features

- [x] Get Plugin active status
- [x] Get Jmeter user status (active/running/stopped)
- [x] Get the list of properties from the running Jmeter (System/Jmeter)
- [x] Update one or more properties to running Jmeter (System/Jmeter)
- [x] Update Logger type to running Jmeter (OFF/FATAL/ERROR/WARN/INFO/DEBUG/TRACE/ALL)
- [x] Get All the threads info 
- [x] Add/Remove users/threads to specific threadgroup(s). Supports multiple updation at single call
- [x] Get info releated to specific threadgroup(s)
- [x] Update info to specific threadgroup(s)
- [x] Get list of variables from one or more threads
- [x] Update variables to one or more threads
- [x] Get elements from the running test (Listeners)
- [ ] Enable/Disable elements to the running test (ThreadGroups/Listeners/ConfigElements etc)
- [x] Stops the test - Supports both gradual and abrupt
- [x] Get Slaves Info
- [x] Send Stop signals to specific/all slaves

## Jar Dependencies Required

* spark-core-2.8.0.jar
* gson-2.2.4.jar

## Jmeter Target

* Jmeter version 5.1.1 or above
* Java 8 or above

## Installation Instructions

* Download the source code from the Github.
* Just do a mvn clean install (Git bash is required)
* Jar will be generated under the target directory (jmeter-onfly-updater-0.1.jar)
* Copy the Jar to `\<Jmeter Installed Directory\>/lib/ext/`

## How to use it
Add required config element (On-Fly-Updater config)

* Set a password for authentication (Default password will be Upd@t3M3)
* Set the Port number on which the Spark Services to run (Optional, Defaults to 1304)
* Set the URI path (Optional, Defaults to /on-fly)
* Once the test started, the Spark server will start a REST server according to the config provided. The default will be `http://127.0.0.1:1304/on-fly/`
* The Rest services supported are as follows. `e.g; http://localhost:1304/on-fly/ping`
* The On-Fly Updater will have a credentials based control to the REST services. It requires a password header to be passed with the REST services.

## Supported REST Services

|Service|HTTP Method|URI|QueryParams|ReqBody|Status|
|:---|:---:|:---|:---|:---|:---:|
PluginRunningStatus|GET|/{URI-PATH}/ping|NA||Completed
GetStatus|GET|/{URI-PATH}/status|NA||Completed
SetLogger|PUT|/{URI-PATH}/logger/{logType}|WARN/ERROR/DEBUG/OFF||Completed
GetProperties|GET|/{URI-PATH}/properties?type={type}|jmeter/system||Completed
UpdateProperties|PUT|/{URI-PATH}/properties|NA||Completed
GetThreads|GET|/{URI-PATH}/threads|NA||Completed
UpdateThreads|PUT|/{URI-PATH}/threads|NA||Completed
GetThreadGroupsList|PUT|/{URI-PATH}/threadgroups|NA||Completed
UpdateThreadGroups|PUT|/{URI-PATH}/threadgroups|NA||Completed
GetJmeterVariables|GET|/{URI-PATH}/vars|NA||Completed
UpdateJmeterVariables|PUT|/{URI-PATH}/vars|NA||Completed
GetElements|GET|/{URI-PATH}/elements|NA||Completed
UpdateTestElement|PUT|/{URI-PATH}/elements|NA||Not Started
StopTest|POST|/{URI-PATH}/stoptest?action={action}|shutdown/stop||Completed
Slaves|GET|/{URI-PATH}/slaves|NA||Completed
StopTest-Slaves|POST|/{URI-PATH}/slaves/stoptest?action={action}|shutdown/stop||Not Started

## Additional Info
Some more info on the plugin

* The REST services will be active only during the test. The End of test will terminate the REST services
* Plugin Running status also initialised during the test
* Observed some abnormal behavior on the plugin when the test is restarted without killing the JMeter JVM

## Known Issues

- [x] Updating thread groups is working, response is not retrieved from the server
- [x] Updating test element in the testplan is not working
- [x] Sending signals to slaves is not working

## References

* Plugin's Core Idea: https://octoperf.com/blog/2020/03/15/beanshell-server
* REST servies: http://sparkjava.com/documentation
* Examples: https://www.baeldung.com/spark-framework-rest-api
* https://stackoverflow.com/questions/51054754/jmeter-ignore-view-results-tree-listener-only-in-non-gui
* https://stackoverflow.com/questions/55796108/jmeter-how-to-disable-listener-by-code-groovy
* 

## Tools Used

* PostMan
* Eclipse
* Markdown editor online (https://dillinger.io/)


## 💲 Donate
<a href="https://www.buymeacoffee.com/rollno748" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/v2/default-green.png" alt="Buy Me A Coffee" style="max-width:20%;" ></a> 

Please rate a :star2: if you like it.

Please open up a :beetle: - If you experienced something.
 


