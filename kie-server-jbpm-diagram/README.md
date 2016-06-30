#kie-server-jbpm-diagram
Server and REST extensions for KIE Exceuction Server that provide capability to serve bpmn diagram for a given process.

###About
This code provides service and REST modules that extend [KIE Execution Server](https://docs.jboss.org/drools/release/latest/drools-docs/html/ch22.html) with capability to serve bpmn diagram content for a given process within a given container.

- **kie-server-jbpm-diagram** - extension's parent project
  - **kie-server-services-jbpm-diagram** - main KIE server extension module that provides diagram service
  - **kie-server-rest-jbpm-diagram** - KIE server component module that provides REST endpoint for a diagram service

Module was tested agains KIE 6.4.0-SNAPSHOT.

####Motivation
Motivation to write this extenion was a need to know details about the processes found in a container deployed in KIE server. Normally, KIE server exposes only basic information from Process Defintion / Instance (i.e. process variables, service tasks, associated entities, active nodes). Getting full picutre of a process (all tasks, flows etc) is not possible in KIE Server and you have to look for information inKIE-Workbench (where process authoring is done) or from kjar artifact from Maven repository.

*NOTE it is possible to fetch complete process diagram image with out-of-the-box jBPM-UI extenion however it provies image in SVG format only - not useful for further processing other than UI*

This extenion enables KIE Server to serve BPMN2 process definition for a given process on request in a similar way how jBPM-UI extenion is serving diagram image.

###Compilation
Use Maven install on a parent project. Successful compilation will result in two jar's (for server extension module and for REST service module).
```
$ git clone https://github.com/mikouaj/droolsjbpm
Cloning into 'droolsjbpm'...
...
Checking connectivity... done.
$ cd droolsjbpm/kie-server-jbpm-diagram
$ mvn clean install
...
[INFO] Reactor Summary:
[INFO]
[INFO] KIE :: Execution Server :: Service and Remote REST for jBPM Diagram SUCCESS [  0.417 s]
[INFO] KIE :: Execution Server :: Services :: jBPM Diagram Extension SUCCESS [  3.612 s]
[INFO] KIE :: Execution Server :: Remote :: REST :: jBPM Diagram SUCCESS [  0.732 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
...
$ find . -name "*.jar" -print
./kie-server-rest-jbpm-diagram/target/kie-server-rest-jbpm-diagram-1.0.0-SNAPSHOT.jar
./kie-server-services-jbpm-diagram/target/kie-server-services-jbpm-diagram-1.0.0-SNAPSHOT.jar
$
```

###Installation
To install modules in KIE Server you simply need to copy both JARs to WEB-INF/lib directory of kie-server.war and deploy it.
```
$ mkdir tmp && cd tmp
$ wget https://repository.jboss.org/org/kie/server/kie-server/6.4.0.Final/kie-server-6.4.0.Final-ee7.war
$ jar xf kie-server-6.4.0.Final-ee7.war
$ cp ../kie-server-rest-jbpm-diagram/target/kie-server-rest-jbpm-diagram-1.0.0-SNAPSHOT.jar ../kie-server-services-jbpm-diagram/target/kie-server-services-jbpm-diagram-1.0.0-SNAPSHOT.jar WEB-INF/lib
$ jar cf kie-server.war docs/ META-INF/ WEB-INF/
$ cp kie-server.war /opt/wildfly/standalone/deployments
```
You can observe server.log on kie-server.war deployment to find traces of successful installation of both modules.
```
$ tail -100f /opt/wildfly/standalone/log/server.log
...
2016-06-28 12:23:53,275 INFO  [org.jboss.weld.deployer] (MSC service thread 1-4) JBAS016008: Starting weld service for deployment kie-server.war
...
2016-06-28 12:23:59,728 INFO  [org.kie.server.services.impl.KieServerImpl] (MSC service thread 1-1) jBPM-Diagram KIE Server extension has been successfully registered as server extension
...
2016-06-28 12:24:00,403 INFO  [org.jboss.resteasy.spi.ResteasyDeployment] (MSC service thread 1-1) Adding singleton resource pl.surreal.kie.server.rest.jbpmdiagram.DiagramResource from Applic
ation class org.kie.server.remote.rest.common.KieServerApplication
...
2016-06-28 12:24:01,346 INFO  [org.jboss.as.server] (ServerService Thread Pool -- 31) JBAS018559: Deployed "kie-server.war" (runtime-name : "kie-server.war")
```
###Usage
Once you have extended kie-server.war, create some container from aftifact that contains bpmn process like org.jbpm.HR from [JBPM playground](https://github.com/droolsjbpm/jbpm-playground).

REST module exposes following endpoint over HTTP:
* [GET] http://serverhost:port/kie-server/services/rest/server/containers/*hr*/processes/*hiring*/diagram

_NOTE notice variables in the url, **hr** is container's ID and **hiring** is process' ID_

####Extended search

Extension is deriving bpmn2 file's name from processId. In most cases it will work fine however it may happen that process file name will be unusual - in that case most accurate way to match ProcessDefinition with bpmn2 file is to check the ID inside the file. Extension is providing *extended search* feature that, if enabled, looks into bpmn2 files to find matching ID. This happens only if regular machanism didnt work.

By default *extended search* is disabled. If bpmn2 file wont be located based on processID then there will be no result. To enable this feature set `pl.surreal.jbpmdiagram.server.extendedsearch.enabled` system property to `TRUE`.

Example for Wildfly/Jboss EAP standalone mode:
* Open your standalone configuration file i.e. `/opt/wildfly/standalone/configuration/standalone-full.xml`
* Locate system property config section (`/server/system-properties`)
* Add new property
```
    <system-properties>
        <property name="pl.surreal.jbpmdiagram.server.extendedsearch.enabled" value="true"/>
    </system-properties>
```
* Reload your app server
* As an altenrative you can this option as argument on server's startup

###Troubleshooting

I expect that most problems with extension maybe be caused by not properly located bpmn2 file. Check *extended search* feature if you didnt enable it yet. If still in trobule, enable DEBUG level for "pl.surreal" category in your AS and observe server log.

Example for Wildfly/Jboss EAP standalone mode:
* Open your standalone configuration file i.e. `/opt/wildfly/standalone/configuration/standalone-full.xml`
* Locate logging subsystem config section (`/server/profile/subsystem[xmlns="urn:jboss:domain:logging:3.0"]`)
* Add new logger category
```
    <logger category="pl.surreal">
      <level name="DEBUG"/>
    </logger>
```
* Reload your app server
