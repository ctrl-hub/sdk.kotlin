plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.serialization") version "2.1.0"
    `maven-publish`
}

group = "org.ctrlhub"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven {
        url = uri("https://maven.scijava.org/content/repositories/public/")
    }
}

dependencies {
    implementation(libs.kotlin.reflect)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.logging.plugin)
    implementation(libs.jetbrains.kotlinx.serialization.json)
    implementation(libs.jsonapi.converter)
    implementation(libs.jackson.datatype.jsr)
    implementation(libs.jackson.kotlin)

    testImplementation(kotlin("test"))
    testImplementation(libs.mockk)
    testImplementation(libs.ktor.client.mock)
}

val generateBuildConfig by tasks.registering {
    val outputDir = layout.buildDirectory.dir("generated/source/buildConfig/com/ctrlhub")
    val packageName = "com.ctrlhub"
    val versionName = project.getGitTag()

    outputs.dir(outputDir)

    doLast {
        val packageDir = outputDir.get().asFile.resolve(packageName.replace('.', '/'))
        packageDir.mkdirs()
        val buildConfigFile = packageDir.resolve("BuildConfig.kt")
        buildConfigFile.writeText(
            """
            package $packageName

            object BuildConfig {
                const val VERSION_NAME = "$versionName"
            }
            """.trimIndent()
        )
    }
}

kotlin.sourceSets["main"].kotlin.srcDir(layout.buildDirectory.dir("generated/source/buildConfig"))

fun Project.getGitTag(): String {
    return try {
        val process = ProcessBuilder("git", "describe", "--tags", "--abbrev=0")
            .redirectErrorStream(true)
            .start()
        process.waitFor(10, TimeUnit.SECONDS)
        process.inputStream.bufferedReader().readText().trim()
    } catch (e: Exception) {
        "0.0.0" // Default version if no tags are found
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/ctrl-hub/sdk.kotlin")

            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }

    publications {
        create<MavenPublication>("gpr") {
            groupId = "com.ctrlhub"
            artifactId = "sdk"
            version = project.getGitTag()

            from(components["kotlin"])
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.named("compileKotlin") {
    dependsOn(generateBuildConfig)
}