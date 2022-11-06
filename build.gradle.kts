import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val vaadinonkotlin_version = "0.14.0"
val vaadin_version = "23.2.6"
val slf4j_version = "2.0.0"

plugins {
    kotlin("jvm") version "1.7.20"
    id("application")
    id("com.vaadin") version "23.2.6"
}

defaultTasks("clean", "build")

repositories {
    mavenCentral()
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

dependencies {
    // Vaadin
    implementation("eu.vaadinonkotlin:vok-framework-vokdb:$vaadinonkotlin_version")
    implementation("org.hibernate.validator:hibernate-validator:6.2.0.Final")
    implementation("com.vaadin:vaadin-core:${vaadin_version}")
    implementation("com.github.mvysny.vaadin-boot:vaadin-boot:10.1")

    // logging
    // currently we are logging through the SLF4J API to slf4j-simple. See src/main/resources/simplelogger.properties file for the logger configuration
    implementation("org.slf4j:slf4j-simple:$slf4j_version")
    implementation("org.slf4j:slf4j-api:$slf4j_version")

    implementation(kotlin("stdlib-jdk8"))

    // db
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.flywaydb:flyway-core:9.5.1")
    implementation("com.h2database:h2:2.1.214")
    implementation("mysql:mysql-connector-java:8.0.28")
    implementation("org.postgresql:postgresql:42.3.3")

    // test support
    testImplementation("com.github.mvysny.kaributesting:karibu-testing-v23:1.3.21")
    testImplementation("com.github.mvysny.dynatest:dynatest:0.24")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

application {
    mainClass.set("com.vaadin.pwademo.MainKt")
}
