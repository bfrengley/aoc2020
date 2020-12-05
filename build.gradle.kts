import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
    id("com.diffplug.spotless") version "5.8.2"
    application
}

group = "me.ben"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

spotless {
    kotlin {
        ktlint().userData(mapOf("end_of_line" to "lf"))
    }
    kotlinGradle {
        ktlint().userData(mapOf("end_of_line" to "lf"))
    }
}

dependencies {
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "13"
}

application {
    mainClassName = "MainKt"
}
