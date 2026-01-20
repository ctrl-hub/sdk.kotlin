package com.ctrlhub.core.datacapture.response

import com.ctrlhub.core.json.JsonConfig
import com.ctrlhub.core.projects.response.Organisation
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.jasminb.jsonapi.StringIdHandler
import com.github.jasminb.jsonapi.annotations.Id
import com.github.jasminb.jsonapi.annotations.Meta
import com.github.jasminb.jsonapi.annotations.Relationship
import com.github.jasminb.jsonapi.annotations.Type
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
@Type("form-schemas")
data class FormSchema @JsonCreator constructor(
    @JsonProperty("id")
    @Id(StringIdHandler::class)
    val id: String? = null,
    @JsonProperty("model")
    val model: Map<String, Any>? = null,
    @JsonProperty("view")
    val view: Map<String, Any>? = null,
    @JsonProperty("version")
    val version: String? = null,
    @Relationship("organisation")
    var organisation: Organisation? = null,
    @Meta
    var meta: FormSchemaMeta? = null,
) {
    val rawSchema: String
        get() {
            val mapper = JsonConfig.getMapper()

            val attributes = mutableMapOf<String, Any>()
            attributes["version"] = version ?: ""
            attributes["id"] = id ?: ""
            model?.let { attributes["model"] = it }
            view?.let { attributes["view"] = it }
            meta?.let { attributes["meta"] = it }

            val dataMap = mutableMapOf(
                "id" to (id ?: ""),
                "type" to "form-schemas",
                "attributes" to attributes
            )

            return mapper.writeValueAsString(dataMap)
        }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class FormSchemaMeta @JsonCreator constructor(
    @JsonProperty("created_at") var createdAt: LocalDateTime,
    @JsonProperty("updated_at") var updatedAt: LocalDateTime?,
    @JsonProperty("latest") var latest: FormSchemaLatestMeta? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class FormSchemaLatestMeta(
    @JsonProperty("id") val id: String = "",
    @JsonProperty("version") val version: String = "",
)