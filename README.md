# Getting Started

## IMPORTANT: use "--recurse-submodules" when cloning

This project relies on on submodules and needs the `--recurse-submodules` switch when cloning the repo in order to retrieve the OpenAPI spec used in these examples.

	git clone --recurse-submodules https://github.com/Tubas118/bdd-java.2022-07.git

If a change occurs to the submodule project, run the following:

	git submodule update

In a real life project, there would be something like Nexus or JFrog to sync up with the OpenAPI spec, but that is not within the scope of these examples.

## Example branches

The following branches exist:

* [AC-1.1-bdd-person-endpoints](/../AC-1.1-bdd-person-endpoints/README.md)
    * Contains the Acceptance Criteria and BDD Requirements in the README file.
    * Simple example of RED and GREEN commit for endpoint to get single person record.


* [AC-1.2-bdd-messy](/../AC-1.2-bdd-messy/README.md)
    * Implementation of person "fuzzy search".
    * Example of iterative RED / GREEN commits while finding issues with the "fuzzy search".


* [AC-1.2-bdd-messy-cleaned-up](/../AC-1.2-bdd-messy-cleaned-up/README.md)
    * Results of using "git rebase" and "git stash" to rewrite the history to one RED and one GREEN commit.
    * End result is exact same code as the final commit of "AC-1.2-bdd-messy".


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

