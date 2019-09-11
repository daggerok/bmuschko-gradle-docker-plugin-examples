plugins {
    idea
    base
    id("com.moowork.node") version "1.3.1"
    id("com.github.ben-manes.versions") version "0.25.0"
}

node {
    download = true
    version = "12.10.0"
    npmVersion = "6.11.3"
}

tasks {
    named<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask>("dependencyUpdates") {
        resolutionStrategy {
            componentSelection {
                all {
                    val rejected = listOf("alpha", "beta", "rc", "cr", "m", "preview", "b", "ea", "SNAPSHOT")
                            .map { qualifier -> Regex("(?i).*[.-]$qualifier[.\\d-+]*") }
                            .any { it.matches(candidate.version) }
                    if (rejected) reject("Release candidate")
                }
            }
        }
    }
}

defaultTasks("clean", "build")
