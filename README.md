# Getting Started

## Example branches

The following branches exist:

* Contains the Acceptance Criteria and BDD Requirements in the README file.
  * Simple example of RED and GREEN commit for endpoint to get single person record.
  * Java 8: [AC-1.1-bdd-person-endpoints.bdd-spec-lib](/../AC-1.1-bdd-person-endpoints.bdd-spec-lib/README.md)
  * Java 17: [java17.AC-1.1-bdd-person-endpoints.bdd-spec-lib](/../java17.AC-1.1-bdd-person-endpoints.bdd-spec-lib/README.md)


* Implementation of person "fuzzy search".
  * Example of iterative RED / GREEN commits while finding issues with the "fuzzy search".
  * Java 8: * [AC-1.2a-bdd-messy.bdd-spec-lib](/../AC-1.2a-bdd-messy.bdd-spec-lib/README.md)
  * Java 17: * [java17.AC-1.2a-bdd-messy.bdd-spec-lib](/../java17.AC-1.2a-bdd-messy.bdd-spec-lib/README.md)


* Results of using "git rebase" and "git stash" to rewrite the history to one RED and one GREEN commit.
  * End result is exact same code as the final commit of "AC-1.2-bdd-messy".
  * Java 8: [AC-1.2-bdd-messy-cleaned-up.bdd-spec-lib](/../AC-1.2-bdd-messy-cleaned-up.bdd-spec-lib/README.md)
  * Java 17: [java17.AC-1.2-bdd-messy-cleaned-up.bdd-spec-lib](/../java17.AC-1.2-bdd-messy-cleaned-up.bdd-spec-lib/README.md)


### Maven commands to check for updates
`mvn versions:update-parent`
`mvn versions:display-dependency-updates versions:display-property-updates`
`mvn versions:display-dependency-updates -DprocessDependencyManagementTransitive=false`
`mvn versions:display-property-updates`

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.1/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.7.1/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.7.1/reference/htmlsingle/#web)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

