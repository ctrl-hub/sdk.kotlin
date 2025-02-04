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
Interaction with the APIs is done via the `Api` class. This class exposes a number of factory methods for instantiating an `Api` object with either a default Ktor client, or a customised one.

```Api.create()```

### Routers
Routers are used to encapsulate specific areas of the API. For example, the `VehiclesRouter` takes care of all Vehicle API interactions. Routers are attached to the `Api` object via extension properties. 

#### Example usage of the `VehiclesRouter`:

```kotlin
val api = Api.create()
val response: List<Vehicle> = api.vehicles.all("your-session-token", "organisation-id", VehicleIncludes.SpecificationModel)
```

## Examples of Usage

```kotlin
Config.environment = Environment.STAGING
val api = Api.create()

api.sessionToken = "sess-123"
val response = api.vehicles.all("org-123", VehicleIncludes.SpecificationModel)
println(response.size)
```

```kotlin
Config.environment = Environment.STAGING
val api = Api.create()

with(api.vehicles) {
    sessionToken = "sess-123"
    val response = all("org-123", VehicleIncludes.SpecificationModel)
    println(response.size)
}
```