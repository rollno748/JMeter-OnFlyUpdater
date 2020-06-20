# Jmeter-OnFlyUpdater

## Introduction
Jmeter plugin to control Jmeter during the running status. This plugin adds feature to control jmeter over REST APIs

## Required Components

1. Jmeter
2. Postman/cURL

## Features

[x] Get Plugin active status
[x] Get Jmeter user status (active/running/stopped)
[x] Get the list of properties from the running Jmeter (System/Jmeter)
[x] Update one or more properties to running Jmeter (System/Jmeter)
[x] Change Logger type to running Jmeter (OFF/FATAL/ERROR/WARN/INFO/DEBUG/TRACE/ALL)
[x] Add/Remove users to specific threadgroup(s). Supports multiple updation at single call
[x] Get info releated to specific threadgroup(s)
[x] Enable/Disable elements to the running test (ThreadGroups/Listeners/ConfigElements etc)
[x] Get list of variables from one or more threads
[x] Update variables to one or more threads

## Supported REST Services

|Service|HTTP Method|URI|QueryParams|ReqBody|Status|
|:---|:---:|:---|:---|:---|:---:|
PluginRunningStatus|GET|/{URI-PATH}/ping|NA||Completed
PropertyDisplay|GET|/{URI-PATH}/properties?type={type}|jmeter/system||Completed
GetStatus|GET|/{URI-PATH}/status|NA||Completed
SetLogger|PUT|/{URI-PATH}/logger/{logType}|WARN/ERROR/DEBUG/OFF||Completed
TestInfo|GET|/{URI-PATH}/testinfo|NA||In Progress
JmeterVariables|GET|/{URI-PATH}/vars|NA||In Progress
UpdateThreads|PUT|/{URI-PATH}/threads|NA||Completed
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

## Known Issues

- [x] Updating test element in the testplan is not working
- [x] Updating variables specific to thread

## References

* Plugin's Core Idea: https://octoperf.com/blog/2020/03/15/beanshell-server
* REST servies: http://sparkjava.com/documentation
* Examples: https://www.baeldung.com/spark-framework-rest-api

## Tools Used

* PostMan
* Eclipse
* Markdown editor online (https://dillinger.io/)


Please rate a :star2: if you like it. Feel free to raise a issue for the bugs you see in this. 

