import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val vaadinonkotlin_version = "0.5.1"
val vaadin10_version = "11.0.1"

plugins {
    kotlin("jvm") version "1.2.71"
    id("org.gretty") version "2.2.0"
    id("io.spring.dependency-management") version "1.0.6.RELEASE"  // remove when https://github.com/gradle/gradle/issues/4417 is fixed
    war
}

defaultTasks("clean", "build")

repositories {
    jcenter()
    maven { setUrl("https://dl.bintray.com/mvysny/github") }
}

gretty {
    contextPath = "/"
    servletContainer = "jetty9.4"
}

dependencyManagement {
    imports { mavenBom("com.vaadin:vaadin-bom:$vaadin10_version") }
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
    compile("com.github.vaadinonkotlin:vok-framework-v10-sql2o:$vaadinonkotlin_version")
    providedCompile("javax.servlet:javax.servlet-api:3.1.0")

    // the app-layout custom component
    compile("org.webjars.bowergithub.polymerelements:app-layout:2.1.0")
    compile("org.webjars.bowergithub.polymerelements:paper-icon-button:2.2.0")

    // logging
    // currently we are logging through the SLF4J API to LogBack. See src/main/resources/logback.xml file for the logger configuration
    compile("ch.qos.logback:logback-classic:1.2.3")
    compile("org.slf4j:slf4j-api:1.7.25")

    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // db
    compile("org.flywaydb:flyway-core:5.2.0")
    compile("com.h2database:h2:1.4.197")

    // test support
    testCompile("com.github.kaributesting:karibu-testing-v10:0.5.2")
    testCompile("com.github.mvysny.dynatest:dynatest-engine:0.11")

    // heroku app runner
    staging("com.github.jsimone:webapp-runner:9.0.11.0")
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
