# Steps to Reproduce cleaned up branch

### Fixup adjacent commits

**Run**

```
git rebase -i origin/main
```
**Output**

```
pick 5f5c1db AC-1.2 - RED: Retrieve fuzzy match persons
pick 4ff7a43 AC-1.2 - RED: Fix retrieve persons fuzzy test
pick 5f1689c AC-1.2 - GREEN: Retrieve fuzzy match persons
pick 0ef79bf AC-1.2 - RED: Oops! Test fails on fuzzy search!
pick 0340149 AC-1.2 - GREEN: Ahh that's better - fuzzy search works!
```

**Mark the commit to "merge" into the previous commit**

Place an "f" on the line for commit `4ff7a43`.

```
pick 5f5c1db AC-1.2 - RED: Retrieve fuzzy match persons
f 4ff7a43 AC-1.2 - RED: Fix retrieve persons fuzzy test
pick 5f1689c AC-1.2 - GREEN: Retrieve fuzzy match persons
pick 0ef79bf AC-1.2 - RED: Oops! Test fails on fuzzy search!
pick 0340149 AC-1.2 - GREEN: Ahh that's better - fuzzy search works!
```

You can see the results by running either  `git log`  or  `git rebase -i origin/main`.

```
pick e30edf0 AC-1.2 - RED: Retrieve fuzzy match persons
pick e6819e2 AC-1.2 - GREEN: Retrieve fuzzy match persons
pick 7a7042e AC-1.2 - RED: Oops! Test fails on fuzzy search!
pick c08ef17 AC-1.2 - GREEN: Ahh that's better - fuzzy search works!
```

### Use `git reset` and `git stash` to reorganize code

**Run**

```
git reset HEAD~1
```

**Output**

```
Unstaged changes after reset:
M       src/main/java/com/example/bdd/java/services/PersonsService.java
```

**Run**

```
git stash
```

**Output**

```
Saved working directory and index state WIP on AC-1.2-bdd-messy-cleaned-up: 7a7042e AC-1.2 - RED: Oops! Test fails on fuzzy search!
```

**Run**

```
$ git reset HEAD~1
```

**Output**

```
Unstaged changes after reset:
M       src/test/java/com/example/bdd/java/controllers/PersonsControllerServiceTests.java
```

**Run**

```
$ git stash
```

**Output**

```
Saved working directory and index state WIP on AC-1.2-bdd-messy-cleaned-up: e6819e2 AC-1.2 - GREEN: Retrieve fuzzy match persons
```

**Run**

```
git stash list
```

**Output**

```
stash@{0}: WIP on AC-1.2-bdd-messy-cleaned-up: e6819e2 AC-1.2 - GREEN: Retrieve fuzzy match persons
stash@{1}: WIP on AC-1.2-bdd-messy-cleaned-up: 7a7042e AC-1.2 - RED: Oops! Test fails on fuzzy search!
```


### Amend existing commits

**Run**

```
git rebase -i origin/main
```

**Output**

```
pick e30edf0 AC-1.2 - RED: Retrieve fuzzy match persons
pick e6819e2 AC-1.2 - GREEN: Retrieve fuzzy match persons
```

**Mark the commit to "merge" into the previous commit**

Place an "e" on the line for commit `e30edf0`.

```
e e30edf0 AC-1.2 - RED: Retrieve fuzzy match persons
pick e6819e2 AC-1.2 - GREEN: Retrieve fuzzy match persons
```

**Pop the updated test off the stash**

```
git stash pop
```

**Example output**

```
interactive rebase in progress; onto f3a0c13
Last commands done (6 commands done):
   pick 66f1d9d BLUE - add test data
   edit e30edf0 AC-1.2 - RED: Retrieve fuzzy match persons
Next command to do (1 remaining command):
   pick e6819e2 AC-1.2 - GREEN: Retrieve fuzzy match persons
  (use "git rebase --edit-todo" to view and edit)
You are currently editing a commit while rebasing branch 'AC-1.2-bdd-messy-cleaned-up' on 'f3a0c13'.
  (use "git commit --amend" to amend the current commit)
  (use "git rebase --continue" once you are satisfied with your changes)

Changes not staged for commit:
  (use "git add <file>..." to update what will be committed)
  (use "git restore <file>..." to discard changes in working directory)
        modified:   src/test/java/com/example/bdd/java/controllers/PersonsControllerServiceTests.java

no changes added to commit (use "git add" and/or "git commit -a")
Dropped refs/stash@{0} (3693837d9197f281c7b57b16b40a0cc8d1a84e36)
```

**Commit the test file into current place of git history**

```
git commit -a -m fixup
```

**Example output**

```
[detached HEAD b153cb2] fixup
 1 file changed, 39 insertions(+), 13 deletions(-)
```

**Continue the rebase**

```
git rebase --continue
```

**Output**

```
Successfully rebased and updated refs/heads/AC-1.2-bdd-messy-cleaned-up.
```


**Pop implementation file from stash**

```
git stash pop
```

**Example output**

```
On branch AC-1.2-bdd-messy-cleaned-up
Changes not staged for commit:
  (use "git add <file>..." to update what will be committed)
  (use "git restore <file>..." to discard changes in working directory)
        modified:   src/main/java/com/example/bdd/java/services/PersonsService.java

no changes added to commit (use "git add" and/or "git commit -a")
Dropped refs/stash@{0} (1c4f707d0f14cda70a2744b2c0e8cef021735d2d)
```

**Commit implementation file into current place in history**

```
git commit -a -m fixup
```

**Example output**

```
[AC-1.2-bdd-messy-cleaned-up 0fac10c] fixup
 1 file changed, 11 insertions(+), 5 deletions(-)
```


### Finalize the history

**Run**

```
git rebase -i origin/main
```

**Output**

```
pick e30edf0 AC-1.2 - RED: Retrieve fuzzy match persons
pick b153cb2 fixup
pick 73ef830 AC-1.2 - GREEN: Retrieve fuzzy match persons
pick 0fac10c fixup
```

**Replace "pick" with "f" on lines with "fixup"**

```
pick e30edf0 AC-1.2 - RED: Retrieve fuzzy match persons
f b153cb2 fixup
pick 73ef830 AC-1.2 - GREEN: Retrieve fuzzy match persons
f 0fac10c fixup
```


**Final history**

```
pick f900089 AC-1.2 - RED: Retrieve fuzzy match persons
pick 388ca33 AC-1.2 - GREEN: Retrieve fuzzy match persons
```

-----------------------------------------------------------------------------



# Acceptance Criteria #1 - Person Endpoints

### Example requirements / BDD ticket

##### REST API Acceptance Criteria

As an app user, I want to lookup a person record, where a record contains an ID, first and last name.

1) Return a single person record when person found by an identifier.

2) Return a list of person records that fuzzy match all provided fields from the search criteria.

3) Return 404 when no person found when using either identifier or search criteria.

----

##### BDD #1 - Return single person record by identifier.

GIVEN:
* the user has a person identifier for a known record

WHEN:
* the user submits the person identifier to the endpoint

THEN:
* the service checks the database using the identifier
* AND finds the person
* AND returns 200 with a single person record as the response.

----

##### BDD #2 - Return a list of person records that FUZZY match all criteria fields.

GIVEN:
* the user has some criteria (ie: first name, last name, id) that should return at least one record

WHEN:
* the user submits the person criteria fields to the endpoint

THEN:
* the service checks the database for all person records
* AND each criteria field provided is checked against the current record's corresponding person field contains that value
* AND adds each matching person to a list
* AND returns 200 with a list of person records as the response.

----

##### BDD #3-a - Return 404 if no person is found using identifier

GIVEN:
* the user has a person identifier for a known record

WHEN:
* the user submits the person identifier to the endpoint
  
THEN:
* the service checks the database using the identifier
* AND does not find a person record
* AND returns 404.

----

##### BDD #3-b - Return 404 if no person is found using search criteria

GIVEN:
* the user has some criteria (ie: first name, last name, id) that should NOT return any records
  
WHEN:
* the user submits the person criteria fields to the endpoint
  
THEN:
* the service checks the database for all person records
* AND finds no matching person records (see BDD #2 for details)
* AND returns 404.

****

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

