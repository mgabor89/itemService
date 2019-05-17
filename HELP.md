# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)

### Guides
The following guides illustrate how to use some features concretely:

* Run unit tests: ./mvnw test -P dev
* Run integration tests: ./mvnw test -P integration-test
* Generate documentation: ./mvnw package. You will find the generated index.html under target/generated-docs
* Launch SpringBoot application: java -jar target/item-api-0.0.1-SNAPSHOT.jar This will start up the webservice and you can send requests to it via http://localhost:8080/item

Please see the generated documentation also, to find out what operations are possible.

##### Right now the project runs with an embedded Tomcat 9, which is the default setting for SpringBoot 2.1.4.
 
