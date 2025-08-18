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

## 4. Coding Style and Conventions
### Router Pattern
- Routers are used to organize API "domains" (e.g., operations, time bands, projects).
- Each router is responsible for a specific resource or domain and provides methods to interact with the corresponding API endpoints.

### Router Structure
- Routers extend a base `Router` class, which provides HTTP utility methods (e.g., `fetchJsonApiResource`, `fetchPaginatedJsonApiResources`).
- Routers are typically constructed with a `HttpClient` instance.

### Request Parameters
- Routers use request parameter classes (e.g., `OperationRequestParameters`, `TimeBandsRequestParameters`) to encapsulate query parameters such as pagination (`offset`, `limit`), filtering, and includes.
- These parameter classes often inherit from abstract base classes like `AbstractRequestParameters` or `RequestParametersWithIncludes` for consistency and code reuse.

### Method Naming and Return Types
- The main method to fetch all resources is named `all` and returns a paginated or full list (e.g., `PaginatedList<Operation>` or `java.util.List<TimeBand>`).
- The method to fetch a single resource is named `one` and returns the resource object (e.g., `Operation`, `TimeBand`).
- Methods are `suspend` functions to support asynchronous calls.

### Extension Properties
- Routers register an extension property on the `Api` class for convenient access (e.g., `val Api.operations: OperationsRouter`, `val Api.timeBands: TimeBandsRouter`).

### Response Models
- Response models are annotated for JSON:API serialization/deserialization using the `jasminb.jsonapi-converter` library.
- IDs are always strings.
- Collections use `java.util.List` for Java compatibility.

### Coding Conventions
- Use idiomatic Kotlin.
- Use `data class` for models.
- Use `@JvmField` for Java interop if needed.
- Use `suspend` functions for API calls to allow for asynchronous programming.
- API responses are annotated using the `jasminb.jsonapi-converter` library for JSONAPI serialization/deserialization.
- IDs are strings, not integers.
- Use `@JvmField` for fields that need to be accessed from Java code.
- Responses that reference a collection use `java.util.List` instead of Kotlin's `List` to ensure compatibility with Java and the JSON API library.
- Routers register an extension function on the `Api` class to provide a convenient way to access the API endpoints.

## 5. Git Commits
- Use conventional commit messages.
- First line of the commit should be 72 characters or fewer.
- Provide a description of the changes in the commit body if necessary.

## 6. Example
- The `OperationsRouter` is a canonical example, with methods for `all` and `one`, a request parameters class supporting includes, and extension properties for access from `Api` and related routers.
