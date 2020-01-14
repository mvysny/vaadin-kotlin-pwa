import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val vaadinonkotlin_version = "0.8.1"
val vaadin10_version = "14.1.5"

plugins {
    kotlin("jvm") version "1.3.61"
    id("org.gretty") version "2.3.1"
    war
    id("com.google.cloud.tools.jib") version "1.7.0"
}

defaultTasks("clean", "build")

repositories {
    jcenter()
}

gretty {
    contextPath = "/"
    servletContainer = "jetty9.4"
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        // to see the exceptions of failed tests in Travis-CI console.
        exceptionFormat = TestExceptionFormat.FULL
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

val staging by configurations.creating

dependencies {
    // Vaadin-on-Kotlin
    compile("eu.vaadinonkotlin:vok-framework-v10-vokdb:$vaadinonkotlin_version")
    compile("com.zaxxer:HikariCP:3.4.1")

    compile(platform("com.vaadin:vaadin-bom:$vaadin10_version"))
    compile("com.vaadin:vaadin-core:${properties["vaadin10_version"]}")
    compile("com.vaadin:flow-server-compatibility-mode:2.1.1")
    providedCompile("javax.servlet:javax.servlet-api:3.1.0")

    // logging
    // currently we are logging through the SLF4J API to slf4j-simple. See src/main/resources/simplelogger.properties file for the logger configuration
    compile("org.slf4j:slf4j-simple:${properties["slf4j_version"]}")
    compile("org.slf4j:slf4j-api:${properties["slf4j_version"]}")

    compile(kotlin("stdlib-jdk8"))

    // db
    compile("org.flywaydb:flyway-core:6.1.4")
    compile("com.h2database:h2:1.4.200")
    compile("mysql:mysql-connector-java:5.1.48")

    // test support
    testCompile("com.github.mvysny.kaributesting:karibu-testing-v10:1.1.19")
    testCompile("com.github.mvysny.dynatest:dynatest-engine:0.15")

    // heroku app runner
    staging("com.github.jsimone:webapp-runner:9.0.27.1")
}

// Heroku
tasks {
    val copyToLib by registering(Copy::class) {
        into("$buildDir/server")
        from(staging) {
            include("webapp-runner*")
        }
    }
    val stage by registering {
        dependsOn("build", copyToLib)
    }
}

// Docker
jib {
    from {
        image = "jetty:9.4.18-jre11"
    }
    container {
        appRoot = "/var/lib/jetty/webapps/ROOT"
    }
}
