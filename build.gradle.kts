import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    // fix for https://github.com/mvysny/vaadin-boot-example-gradle/issues/3
    dependencies {
        classpath("com.vaadin:vaadin-prod-bundle:${project.properties["vaadinVersion"]}")
    }
}

plugins {
    kotlin("jvm") version "1.9.0"
    id("application")
    id("com.vaadin")
}

defaultTasks("clean", "build")

repositories {
    mavenCentral()
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        // when tests fail, we want to see the exceptions on stdout
        exceptionFormat = TestExceptionFormat.FULL
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

dependencies {
    // Vaadin
    implementation("eu.vaadinonkotlin:vok-framework-vokdb:${properties["vokVersion"]}") {
        exclude(group = "com.vaadin")
    }
    implementation("com.github.mvysny.karibudsl:karibu-dsl-v23:2.0.1")
    implementation("com.vaadin:vaadin-core:${properties["vaadinVersion"]}") {
        afterEvaluate {
            if (vaadin.productionMode) {
                exclude(module = "vaadin-dev")
            }
        }
    }
    implementation("com.github.mvysny.vaadin-boot:vaadin-boot:11.3")

    // validator. We need to explicitly declare it since we're using annotations from it
    implementation("org.hibernate.validator:hibernate-validator:8.0.0.Final")

    // logging
    // currently we are logging through the SLF4J API to slf4j-simple. See src/main/resources/simplelogger.properties file for the logger configuration
    implementation("org.slf4j:slf4j-simple:2.0.7")

    implementation(kotlin("stdlib-jdk8"))

    // db
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.flywaydb:flyway-core:9.15.2")
    implementation("com.h2database:h2:2.1.214")

    // test support
    testImplementation("com.github.mvysny.kaributesting:karibu-testing-v24:2.1.0")
    testImplementation("com.github.mvysny.dynatest:dynatest:0.24")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

application {
    mainClass.set("com.vaadin.pwademo.MainKt")
}
