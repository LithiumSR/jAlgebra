import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    kotlin("jvm") version "1.3.61"
    id("org.gretty") version "3.0.1"
    war
}

defaultTasks("clean", "build")

repositories {
    jcenter()
    mavenCentral()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

gretty {
    contextPath = "/"
    servletContainer = "tomcat9"
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        // to see the exceptions of failed tests in the CI console.
        exceptionFormat = TestExceptionFormat.FULL
    }
}

dependencies {
    implementation("io.javalin:javalin:3.7.0") {
        exclude(mapOf("group" to "org.eclipse.jetty"))
        exclude(mapOf("group" to "org.eclipse.jetty.websocket"))
    }
    implementation("org.slf4j:slf4j-simple:1.7.30")
    implementation (   "com.fasterxml.jackson.core:jackson-databind:2.10.1")
    implementation ("org.json:json:20201115")
    testImplementation ("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine:5.3.1")
}
