import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val vaadinonkotlin_version = "0.7.0"
val vaadin10_version = "13.0.0.beta1"

plugins {
    kotlin("jvm") version "1.3.21"
    id("org.gretty") version "2.2.0"
    war
}

defaultTasks("clean", "build")

repositories {
    mavenCentral()
    maven { setUrl("https://maven.vaadin.com/vaadin-prereleases/") }  // because of Vaadin 13.0.0.beta1
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
    // Vaadin-on-Kotlin dependency, includes Vaadin
    compile("eu.vaadinonkotlin:vok-framework-v10-sql2o:$vaadinonkotlin_version")
    compile(enforcedPlatform("com.vaadin:vaadin-bom:$vaadin10_version"))
    providedCompile("javax.servlet:javax.servlet-api:3.1.0")

    // the app-layout custom component
    compile("org.webjars.bowergithub.polymerelements:app-layout:2.1.0")
    compile("org.webjars.bowergithub.polymerelements:paper-icon-button:2.2.0")

    // logging
    // currently we are logging through the SLF4J API to LogBack. See src/main/resources/logback.xml file for the logger configuration
    compile("ch.qos.logback:logback-classic:1.2.3")
    compile("org.slf4j:slf4j-api:1.7.25")

    compile(kotlin("stdlib-jdk8"))

    // db
    compile("org.flywaydb:flyway-core:5.2.0")
    compile("com.h2database:h2:1.4.197")

    // test support
    testCompile("com.github.mvysny.kaributesting:karibu-testing-v10:1.1.2")
    testCompile("com.github.mvysny.dynatest:dynatest-engine:0.13")

    // heroku app runner
    staging("com.github.jsimone:webapp-runner:9.0.14.0")
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
