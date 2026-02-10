import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.3.10"
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
    compilerOptions.jvmTarget = JvmTarget.JVM_21
}

dependencies {
    // Vaadin
    implementation(libs.ktorm.vaadin)
    implementation(libs.vaadin.core) 
    if (!vaadin.effective.productionMode.get()) {
        implementation(libs.vaadin.dev)
    }
    implementation(libs.vaadinboot)
    implementation(libs.karibu.dsl)

    // validator. We need to explicitly declare it since we're using annotations from it
    implementation(libs.hibernate.validator)

    // logging
    // currently we are logging through the SLF4J API to slf4j-simple. See src/main/resources/simplelogger.properties file for the logger configuration
    implementation(libs.slf4j.simple)

    // db
    implementation(libs.hikaricp)
    implementation(libs.flyway)
    implementation(libs.h2)

    // test support
    testImplementation(libs.karibu.testing)
    testImplementation(libs.junit)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

application {
    mainClass = "com.vaadin.pwademo.MainKt"
}
