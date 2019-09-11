import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.bmuschko.gradle.docker.tasks.image.*
import org.apache.tools.ant.taskdefs.ExecTask

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.3.50"
    id("com.bmuschko.docker-remote-api") version "5.0.0"
}

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

repositories {
    // mavenLocal()
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
}

kotlin.sourceSets["main"].kotlin.srcDirs("src")
kotlin.sourceSets["test"].kotlin.srcDirs("test")

sourceSets["main"].resources.srcDirs("resources")
sourceSets["test"].resources.srcDirs("testresources")

docker {
    registryCredentials {
        username.set("daggerok")
        email.set("daggerok@gmail.com")
        password.set(System.getProperty("password", "change me"))
    }
}

tasks {
    val dockerfile = register("dockerfile", com.bmuschko.gradle.docker.tasks.image.Dockerfile::class) {
        from("openjdk:14-ea-12-jdk-alpine3.10")
        label(mapOf("MAINTAINER" to "Maksim Kostromin <daggerok@gmail.com>"))
        instruction("HEALTHCHECK CMD wget --quiet --tries=1 --spider http://127.0.0.1:8080/ || exit 1")
        exposePort(8080)
        defaultCommand("/bin/ash")
        val applicationDir = "/tmp/${project.name}"
        entryPoint("ash", "${applicationDir}/bin/${project.name}")
        workingDir(applicationDir)
        copyFile("./build/install/${project.name}", applicationDir)
        destFile.set(file("./Dockerfile"))
    }
    val imageName = "daggerok/docker-remote-api-app:${project.version}"
    val dockerBuild by creating(com.bmuschko.gradle.docker.tasks.image.DockerBuildImage::class) {
        dependsOn(installDist, dockerfile)
        inputDir.set(file("."))
        // inputDir.set(project.buildDir)
        dockerFile.set(dockerfile.get().destFile)
        tags.add(imageName)
    }
    val appContainerName = "run-${project.name}"
    val dockerCreateContainer by creating(com.bmuschko.gradle.docker.tasks.container.DockerCreateContainer::class) {
        targetImageId(imageName)
        portBindings.set(listOf("8080:8080"))
        containerName.set(appContainerName)
        autoRemove.set(true)
    }
    val dockerStartContainer by creating(com.bmuschko.gradle.docker.tasks.container.DockerStartContainer::class) {
        dependsOn(dockerCreateContainer)
        targetContainerId(dockerCreateContainer.containerId)
    }
    val dockerStart by creating(com.bmuschko.gradle.docker.tasks.container.extras.DockerWaitHealthyContainer::class) {
        dependsOn(dockerStartContainer)
        targetContainerId(dockerCreateContainer.containerId)
    }
    val dockerStopContainer by creating(com.bmuschko.gradle.docker.tasks.container.DockerStopContainer::class) {
        targetContainerId(dockerCreateContainer.containerId)
    }
    val dockerRm by creating(com.bmuschko.gradle.docker.tasks.container.DockerRemoveContainer::class) {
        targetContainerId(appContainerName)
        onError {
            // Ignore exception if container does not exist otherwise throw it
            if (!this.message!!.contains("No such container"))
                throw this
        }
    }
    // register("rm", Exec::class) {
    //     commandLine("docker", "rm", "-f", "-v", "run-${project.name}")
    // }
}

defaultTasks("installDist")
