plugins {
    kotlin("jvm") version "2.0.21"
}

group = "org.ctrlhub"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}