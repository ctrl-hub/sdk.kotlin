plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.serialization") version "2.1.0"
}

group = "org.ctrlhub"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlin.reflect)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.jetbrains.kotlinx.serialization.json)

    testImplementation(kotlin("test"))
    testImplementation(libs.mockk)
    testImplementation(libs.ktor.client.mock)
}

tasks.test {
    useJUnitPlatform()
}