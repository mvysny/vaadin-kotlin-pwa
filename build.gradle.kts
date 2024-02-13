import com.vaadin.gradle.getBooleanProperty
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.22"
    application
    alias(libs.plugins.vaadin)
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
    implementation(libs.vok.db)
    implementation(libs.vaadin.core) {
        // https://github.com/vaadin/flow/issues/18572
        if (vaadin.productionMode.map { v -> getBooleanProperty("vaadin.productionMode") ?: v }.get()) {
            exclude(module = "vaadin-dev")
        }
    }
    implementation(libs.vaadin.boot)

    // validator. We need to explicitly declare it since we're using annotations from it
    implementation(libs.hibernate.validator)

    // logging
    // currently we are logging through the SLF4J API to slf4j-simple. See src/main/resources/simplelogger.properties file for the logger configuration
    implementation(libs.slf4j.simple)

    implementation(kotlin("stdlib-jdk8"))

    // db
    implementation(libs.hikaricp)
    implementation(libs.flyway)
    implementation(libs.h2)

    // test support
    testImplementation(libs.karibu.testing)
    testImplementation(libs.dynatest)
    testRuntimeOnly(libs.junit.platform.launcher)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

application {
    mainClass = "com.vaadin.pwademo.MainKt"
}
