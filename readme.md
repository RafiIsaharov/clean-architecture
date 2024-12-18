## Installation

### Generate sources
Maven install to generate sources
1. From IntelliJ > View > Tool Windows > Maven > Lifecycle > install
2. From command line: `mvn clean install -DskipTests`

To Add generated sources in IntelliJ, right-click the following folders > Mark as > Generated Sources Root
- `clean-application/target/generated-sources/annotations` for MapStruct (CustomerMapStructImpl)
- `clean-application/target/generated-sources/openapi/src/main/java` for OpenAPI (LdapUserDto,...)

Marking folders as "Generated Sources Root" in IntelliJ IDEA tells the IDE that these folders contain source files that were generated automatically and should be treated as part of the project's source code. This allows IntelliJ to include these files in code completion, navigation, and other IDE features, just like manually written source files.

### TODO: Extract infrastructure concerns from `domain` package
NotificationService is 'contaminated' with infrastructure concerns. 
Extract all that has to do with LDAP out of the domain package.
Enable the @Disabled ArchitectureTest and make it pass.

