# Ctrl Hub Kotlin SDK

*Please note: This SDK is a work in progress, and as such, this README could change at any time*

## Overview

The `sdk.kotlin` repository provides an SDK implemented in Kotlin for integrating with the Ctrl Hub APIs. The SDK interfaces with the Ctrl Hub APIs using the Ktor library.

## Usage

Include the SDK via Maven/Gradle. Package information is available [here](https://github.com/ctrl-hub/sdk.kotlin/packages/2363087).

Example Gradle config:

```declarative
repositories {
    google()
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/ctrl-hub/sdk.kotlin")
        credentials {
            username = "your-github-username"
            password = "your-personal-access-token"
        }
    }
}

dependencies {
    implementation "com.ctrlhub:your-sdk-version"
}
```
Please note: although the SDK is available publicly through GitHub, GitHub's Maven repository requires a GitHub personal access token to make use of it.

### Configuration

Configuring the SDK to use either staging or production APIs is as simple as re-assigning the `environment` property:

```kotlin
Config.environment = Environment.STAGING    // For staging
Config.environment = Environment.PRODUCTION // For production
```

### API object
Interaction with the APIs is done via the `Api` singleton. The Api singleton provides access to a configured Ktor client for interaction with the Api. A session token can also be applied:

```kotlin
Api.sessionToken = "valid_session_token"
```

### Routers
Routers are used to encapsulate specific areas of the API. For example, the `VehiclesRouter` takes care of all Vehicle API interactions. Routers are attached to the `Api` object via extension properties. 

#### Example usage of the `VehiclesRouter`:

```kotlin
val api = Api.create()
val response: List<Vehicle> = api.vehicles.all("organisation-id", VehicleRequestParameters(
    includes = listOf(
        VehicleIncludes.Specification
    )
))
```

## Examples of Usage

```kotlin
Config.environment = Environment.STAGING
Api.sessionToken = "" // Replace with an actual session token

runBlocking {
    val response = Api.vehicles.all("123")
    println(response.size)
}
```