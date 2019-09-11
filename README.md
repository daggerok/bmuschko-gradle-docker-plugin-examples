# bmuschko-gradle-docker-plugin-examples
Testing bmuschko/gradle-docker-plugin (requires: java and docker) [![Build Status](https://travis-ci.org/daggerok/bmuschko-gradle-docker-plugin-examples.svg?branch=master)](https://travis-ci.org/daggerok/bmuschko-gradle-docker-plugin-examples)

[[toc]]

## VuePress docs

```bash
./gradlew docker-remote-api-app:installDist
./gradlew docker-remote-api-app:dockerFile
./gradlew docker-remote-api-app:dockerBuild
```

## VuePress docs

```bash
./gradlew npm_i
./gradlew npm_run_build
npx serve .vuepress/dist/
```

open http://localhost:5000/

Documentation is deployed on [GitHub Pages](https://daggerok.github.io/bmuschko-gradle-docker-plugin-examples/)

## resources

* [Gradle Docker Plugin User Guide & Examples](https://bmuschko.github.io/gradle-docker-plugin/)
* https://bmuschko.com/blog/docker-integration-testing/
* https://bmuschko.com/blog/java-project-testcontainers/
* https://bmuschko.com/blog/dockerized-spring-boot-app/
* https://bmuschko.com/blog/asciidoctorj-extension/
* https://bmuschko.com/blog/go-on-jenkins/

