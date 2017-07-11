# Digger Java client

[![Build Status](https://travis-ci.org/aerogear/digger-java.png)](https://travis-ci.org/aerogear/digger-java)
[![License](https://img.shields.io/:license-Apache2-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

A java integration library for AeroGear Digger

## Project Info

|                 | Project Info  |
| --------------- | ------------- |
| License:        | Apache License, Version 2.0  |
| Build:          | Maven  |
| Documentation:  | https://github.com/aerogear/digger-java  |
| Issue tracker:  | https://issues.jboss.org/browse/AGDIGGER  |
| Mailing lists:  | [aerogear-users](http://aerogear-users.1116366.n5.nabble.com/) ([subscribe](https://lists.jboss.org/mailman/listinfo/aerogear-users))  |
|                 | [aerogear-dev](http://aerogear-dev.1069024.n5.nabble.com/) ([subscribe](https://lists.jboss.org/mailman/listinfo/aerogear-dev))  |
| IRC:            | [#aerogear](https://webchat.freenode.net/?channels=aerogear) channel in the [freenode](http://freenode.net/) network.  |


## Getting started

For now, clone the repository and run the build:

```
mvn clean install
```

Add the following dependencies to your ```pom.xml``` file:

```xml
<dependency>
    <groupId>org.jboss.aerogear</groupId>
    <artifactId>digger-java-client</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## Usage

Build a default client:
```
   DiggerClient client = DiggerClient.createDefaultWithAuth("https://digger.com", "admin", "password");
```

Build a customized client:
```
   DiggerClient client = DiggerClient.builder()
         .jobService(new JobService())
         .buildService(new BuildService(10000, 100))
         .artifactsService(artifactsService)
         .withAuth("https://digger.com", "admin", "password")
         .build();
```

Create job:

```
  client.createJob("java-client-job1","https://github.com/wtrocki/helloworld-android-gradle","master");
```

The client supports setting clean-up values for jobs in relation to the number of days to store builds and artifacts
and the total number of builds and artifacts to keep.

Create a job with clean-up policy:
```java
import org.aerogear.digger.client.model.BuildDiscarder

BuildDiscarder buildDiscarder = new BuildDiscarder();
buildDiscarder.setStoreBuildsDays(7)
client.createJob("java-client-job1","https://github.com/wtrocki/helloworld-android-gradle","master", buildDiscarder);
```


The client supports passing a list of build parameters. To create a job with build params:
```java
import org.aerogear.digger.client.model.BuildParameter;

public static final BuildParameter EXAMPLE = new BuildParameter(EXAMPLE)
   .setDescription("Branch of project to checkout and build.")
   .setDefaultValue("master");

private List<BuildParameter> params = Lists.newArrayList(EXAMPLE)

client.createJob("java-client-job1","https://github.com/wtrocki/helloworld-android-gradle","master", params);

```


Trigger a job:

```
  BuildStatus buildStatus = client.build("java-client-job1");
```

Get build logs:

```
  int buildNumber = 4;
  String logs = client.getBuildLogs("java-client-job1", buildNumber);
```

Save artifacts to file

```
  int buildNumber = 4;
  String artifactName="*.apk" 
  File outputFile = ...
  client.saveArtifact("java-client-job1", buildNumber,artifactName, outputFile);
```

## Requirements

Client works with Java6 and above.

## Building

`mvn clean package`

