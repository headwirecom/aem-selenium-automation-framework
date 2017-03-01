# AEM Test Framework

### Installation

To use the framework in a maven project, first you must connect to the CQ Blueprints Maven Repository and add the following dependencies:

```
<dependency>
    <groupId>com.cqblueprints.testing</groupId>
    <artifactId>test-framework</artifactId>
    <version>0.6.0</version>
</dependency>
```
The jar file can be downloaded dirctly [here](http://dev.cqblueprints.com/nexus/service/local/repositories/releases/content/com/cqblueprints/testing/test-framework/0.1.6/test-framework-0.1.6-dependencies.jar)

### Test Environment Setup
The framework loads the test environment from a json file. The structure of the file is as follows:

```
{
  "authorUrl" : "http://localhost:4502",
  "publishUrl" : "http://localhost:4503",
  "testUser" : "admin",
  "testPassword" : "admin",
  "version" : "6.3",
  "browser" : "firefox",
  "proxyEnabled" : "true"
}
```

By default, the framework will use a settings.json file located in the root directory of the project. Alternative locations for the file can be specified by using the TestEnvironmentLoader:

The authorUrl and publishUrl parameters point to the instance under test and the user and password refer to a user with the correct ACL to create, edit, and delete pages under the test page path. The confguration also contains the version of AEM being tested. Valid values for browser are firefox, chrome, ie and html. HTML is a headless version of a browser and is a quick way to run tests if an actual browser is not required. The last settings parameter determines if a browsermob-proxy will be set up to intercept requests. A value of a "true" will configure the proxy and apply BASIC authentication to requests so the tests will not need to log in. Additional information on the proxy can be found here

### Usage

The testing framework uses JUnit and follows the same conventions for writing tests. To begin using the built in functionality, the test should extend the TestBase class and use the provided page objects which expose high level AEM functions that will simplify test writing and reduce the need to use low level selenium in your test cases.

The TestBase uses the environment variable to setup and instantiate the WebDriver. A proxy is loaded and connected to the WebDriver which will add authentication info to each request and collects performance data.

### More Information
The project is hosted by cqblueprints and more information can be found on their [site](http://www.cqblueprints.com/content/blueprints/en/tipsandtricks/aem-test-framework.html)

### Examples
More examples can be found in the [aem-selenium-automation-examples](https://github.com/headwirecom/aem-selenium-automation-framework-example) project
