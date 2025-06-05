package com.ctrlhub.core.datacapture

import com.ctrlhub.core.Api
import com.ctrlhub.core.api.response.PaginatedList
import com.ctrlhub.core.datacapture.response.FormSchema
import com.ctrlhub.core.datacapture.response.FormSchemaMeta
import com.ctrlhub.core.extractPaginationFromMeta
import com.ctrlhub.core.router.Router
import com.ctrlhub.core.router.request.RequestParameters
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import kotlinx.serialization.json.*
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class FormSchemasRouter(httpClient: HttpClient) : Router(httpClient) {

    suspend fun all(
        organisationId: String,
        formId: String,
        requestParameters: RequestParameters = RequestParameters()
    ): PaginatedList<FormSchema> {
        val endpoint = "/v3/orgs/$organisationId/data-capture/forms/$formId/schemas"

        val response = performGet(endpoint, requestParameters.toMap())
        val jsonContent = Json.parseToJsonElement(response.body<String>()).jsonObject

        val dataArray = jsonContent["data"]?.jsonArray ?: JsonArray(emptyList())
        val formSchemas = dataArray.mapNotNull { item ->
            item.jsonObjectOrNull()?.let { instantiateFormSchemaFromJson(it) }
        }

        return PaginatedList(
            data = formSchemas,
            pagination = extractPaginationFromMeta(jsonContent)
        )
    }

    suspend fun one(
        organisationId: String,
        formId: String,
        schemaId: String,
        requestParameters: RequestParameters = RequestParameters()
    ): FormSchema {
        val endpoint = "/v3/orgs/$organisationId/data-capture/forms/$formId/schemas/$schemaId"

        val response = performGet(endpoint, requestParameters.toMap())
        val jsonContent = Json.parseToJsonElement(response.body<String>()).jsonObjectOrNull()
            ?: throw IllegalStateException("Missing JSON content")

        return instantiateFormSchemaFromJson(jsonContent)
    }

    private fun instantiateFormSchemaFromJson(json: JsonObject): FormSchema {
        val id = json["id"]?.jsonPrimitive?.content
            ?: throw IllegalStateException("Missing id")

        val rawContent = json.toString()
        val isoFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

        val formSchemaMeta = json["meta"]?.jsonObject?.let {
            val createdAtStr = it["created_at"]?.jsonPrimitive?.content
            val updatedAtStr = it["updated_at"]?.jsonPrimitive?.contentOrNull

            FormSchemaMeta(
                createdAt = createdAtStr?.let { ZonedDateTime.parse(it, isoFormatter).toLocalDateTime() }
                    ?: throw IllegalStateException("Missing created_at"),
                updatedAt = updatedAtStr?.takeIf { it.isNotEmpty() }?.let {
                    ZonedDateTime.parse(it, isoFormatter).toLocalDateTime()
                },
                latest = it["latest"]?.jsonPrimitive?.content.orEmpty()
            )
        }

        return FormSchema(
            id = id,
            rawSchema = rawContent,
            meta = formSchemaMeta,
        )
    }

    private fun JsonElement.jsonObjectOrNull(): JsonObject? = this as? JsonObject
}

val Api.formSchemas: FormSchemasRouter
    get() = FormSchemasRouter(httpClient)