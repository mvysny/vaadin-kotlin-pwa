import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val vaadinonkotlin_version = "0.10.0"
val vaadin10_version = "14.5.3"
val slf4j_version = "1.7.30"

plugins {
    kotlin("jvm") version "1.4.32"
    id("org.gretty") version "3.0.4"
    war
    id("com.vaadin") version "0.14.5.1"
    id("com.google.cloud.tools.jib") version "3.0.0"
}

defaultTasks("clean", "build")

repositories {
    mavenCentral()
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
    implementation("eu.vaadinonkotlin:vok-framework-v10-vokdb:$vaadinonkotlin_version")
    implementation("org.hibernate.validator:hibernate-validator:6.1.4.Final")

    // Vaadin 14
    implementation("com.vaadin:vaadin-core:${vaadin10_version}") {
        // Webjars are only needed when running in Vaadin 13 compatibility mode
        listOf("com.vaadin.webjar", "org.webjars.bowergithub.insites",
                "org.webjars.bowergithub.polymer", "org.webjars.bowergithub.polymerelements",
                "org.webjars.bowergithub.vaadin", "org.webjars.bowergithub.webcomponents")
                .forEach { exclude(group = it) }
    }
    providedCompile("javax.servlet:javax.servlet-api:3.1.0")

    // logging
    // currently we are logging through the SLF4J API to slf4j-simple. See src/main/resources/simplelogger.properties file for the logger configuration
    implementation("org.slf4j:slf4j-simple:$slf4j_version")
    implementation("org.slf4j:slf4j-api:$slf4j_version")

    implementation(kotlin("stdlib-jdk8"))

    // db
    implementation("com.zaxxer:HikariCP:3.4.5")
    implementation("org.flywaydb:flyway-core:7.1.1")
    implementation("com.h2database:h2:1.4.200")
    implementation("mysql:mysql-connector-java:5.1.48")
    implementation("org.postgresql:postgresql:42.2.1")

    // test support
    testImplementation("com.github.mvysny.kaributesting:karibu-testing-v10:1.2.12")
    testImplementation("com.github.mvysny.dynatest:dynatest-engine:0.19")

    // heroku app runner
    staging("com.heroku:webapp-runner:9.0.41.0")
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

vaadin {
    if (gradle.startParameter.taskNames.contains("stage")) {
        productionMode = true
    }
}

// Docker
jib {
    from {
        image = "jetty:9.4.40-jre11"
    }
    container {
        appRoot = "/var/lib/jetty/webapps/ROOT"
        user = "root" // otherwise we'll get https://github.com/appropriate/docker-jetty/issues/80
    }
}
