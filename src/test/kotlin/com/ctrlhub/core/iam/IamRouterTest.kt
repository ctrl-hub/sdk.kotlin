package com.ctrlhub.core.iam

import com.ctrlhub.core.api.KtorApiClient
import com.ctrlhub.core.iam.response.User
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class IamRouterTest {

    @Test
    fun `test retrieve whoami user`() {
        val jsonFilePath = Paths.get("src/test/resources/iam/whoami-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val iamRouter = IamRouter(
            apiClient = KtorApiClient.create(HttpClient(mockEngine))
        )

        runBlocking {
            val response = iamRouter.whoami(sessionToken = "test-token")
            assertIs<User>(response)
            assertEquals("00000000-0000-0000-0000-000000000000", response.id)
            assertEquals("user@example.com", response.email)
            assertEquals("Test", response.profile.personal.firstName)
            assertEquals("User", response.profile.personal.lastName)
        }
    }
}