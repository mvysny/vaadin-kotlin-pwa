import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val vaadinonkotlin_version = "0.12.0"
val vaadin_version = "23.0.0.alpha1"
val slf4j_version = "1.7.32"

plugins {
    kotlin("jvm") version "1.6.10"
    id("org.gretty") version "3.0.6"
    war
    id("com.vaadin") version "23.0.0.alpha1"
    id("com.google.cloud.tools.jib") version "3.0.0"
}

defaultTasks("clean", "build")

repositories {
    mavenCentral()
    maven { setUrl("https://maven.vaadin.com/vaadin-prereleases") }
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
    kotlinOptions.jvmTarget = "11"
}

val staging by configurations.creating

dependencies {
    // Vaadin-on-Kotlin
    implementation("eu.vaadinonkotlin:vok-framework-vokdb:$vaadinonkotlin_version")
    implementation("org.hibernate.validator:hibernate-validator:6.2.0.Final")

    // Vaadin 14
    implementation("com.vaadin:vaadin-core:${vaadin_version}")
    providedCompile("javax.servlet:javax.servlet-api:3.1.0")

    // logging
    // currently we are logging through the SLF4J API to slf4j-simple. See src/main/resources/simplelogger.properties file for the logger configuration
    implementation("org.slf4j:slf4j-simple:$slf4j_version")
    implementation("org.slf4j:slf4j-api:$slf4j_version")

    implementation(kotlin("stdlib-jdk8"))

    // db
    implementation("com.zaxxer:HikariCP:4.0.3")
    implementation("org.flywaydb:flyway-core:8.4.1")
    implementation("com.h2database:h2:2.0.206")
    implementation("mysql:mysql-connector-java:5.1.48")
    implementation("org.postgresql:postgresql:42.2.1")

    // test support
    testImplementation("com.github.mvysny.kaributesting:karibu-testing-v10:1.3.9")
    testImplementation("com.github.mvysny.dynatest:dynatest:0.22")

    // heroku app runner
    staging("com.heroku:webapp-runner:9.0.52.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
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
