# AI Overview: Kotlin SDK

## 1. High Level Overview
The Kotlin SDK is an SDK that provides a way to interact with the CtrlHub APIs using Kotlin.

## 2. Overview of Technologies
- **Kotlin**
- **Gradle**: Build tool for Kotlin projects.
- **Ktor**: Asynchronous HTTP client for making API requests.

This is not a KMP project.

## 3. Project Structure
- `src/main/kotlin`: Contains the main Kotlin code for the SDK.
- `src/test/kotlin`: Contains the test code for the SDK.
- `build.gradle.kts`: Gradle build file for the project.
- `settings.gradle.kts`: Gradle settings file for the project.

## 4. Coding Conventions
- Use Kotlin's idiomatic style.
- Use Routers for organising API "domains". A domain is a particular resource that the API supports. Example: `projects`.
- Use `suspend` functions for API calls to allow for asynchronous programming.
- Use `data class` for models to represent API responses.
- API responses are annotated using the `jasminb.jsonapi-converter` library for JSONAPI serialization/deserialization.
- IDs are strings, not integers.
- Use `@JvmField` for fields that need to be accessed from Java code.
- Responses that reference a collection use `java.util.List` instead of Kotlin's `List` to ensure compatibility with Java and the JSON API library.
- Routers register an extension function on the `Api` class to provide a convenient way to access the API endpoints.

## Git Commits
- Use conventional commit messages.
- First line of the commit should be 72 characters or less.
- Provide a description of the changes in the commit body if necessary.

## 7. Examples
Given the following JSON snippet:
```json
{
  "data": {
    "type": "projects",
    "id": "123",
    "attributes": {
      "name": "My Project"
    }
  }
}
```

You can create a data class to represent this response:

```kotlin
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.jasminb.jsonapi.StringIdHandler
import com.github.jasminb.jsonapi.annotations.Id
import com.github.jasminb.jsonapi.annotations.Relationship
import com.github.jasminb.jsonapi.annotations.Type

@Type("projects")
@JsonIgnoreProperties(ignoreUnknown = true)
data class Project @JsonCreator constructor(
    @Id(StringIdHandler::class) var id: String = "",
    @JsonProperty("name") var name: String = ""
)
```