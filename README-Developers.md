# Nifty SunSpec - Developers Guide

This document has information for developers of Nifty SunSpec.

# Running tests

Run all the tests, and generate coverage reports in each project's `build/reports/jacoco/test`
directory, like this:

```sh
./gradlew check
```

# Publishing to Local Maven repository

You can publish the artifacts to your local Maven repository (e.g. `~/.m2/repository`) like this:

```sh
./gradlew publishToMavenLocal
```

# Publishing to Maven Central

To publish to Maven Central use the `publish` Gradle task. You must provide your Sonatype
credentials and the GPG signing key to use as project properties:

| Property | Description |
|:---------|:------------|
| `signing.gnupg.keyName` | The GPG signing key ID to use. |
| `ossrhUsername` | The Sonatype username. |
| `ossrhPassword` | The Sonatype password. |

You can provide these using `-P` command line arguments, e.g.

```sh
./gradlew publish -Psigning.gnupg.keyName=ABC123DEF -PossrhUsername=user -PossrhPassword=pass
```

Alternatively you can create a `gradle.properties` file (which will be ignored by Git) and place
the properties there, e.g.

```
signing.gnupg.keyName = ABC123DEF
ossrhUsername = user
ossrhPassword = password
```

Alternatively you can also provide the credentials via environment variables (the signing key, as it
contains dot characters, cannot so easily be provided this way):

 * `ORG_GRADLE_PROJECT_ossrhUsername` - the username
 * `ORG_GRADLE_PROJECT_ossrhPassword` - the password

For example, you could create a small shell script `nifty-publish.env`:

```sh
export ORG_GRADLE_PROJECT_ossrhUsername="my username"
export ORG_GRADLE_PROJECT_ossrhPassword="my password"
```

Then you can do this to publish:

```sh
source nifty-publish.env
./gradlew publish -Psigning.gnupg.keyName=ABC123DEF
```

# Release script

The `config/tools/release.sh` script can be used to update the project version and create a new
release tag. For example to release version `1.0.0`:

```sh
./config/tools/release 1.0.0
```

You can preview what the script does with a `-n` option:

```sh
RELEASE: DRY RUN: no changes will be made.
RELEASE: start release 1.0.0
RELEASE: Update build.gradle version to 1.0.0
RELEASE: commit release version changes.
RELEASE: Update build.gradle version to 1.0.1-dev.0
RELEASE: commit development version changes.
RELEASE: switch to main for publish.
```
