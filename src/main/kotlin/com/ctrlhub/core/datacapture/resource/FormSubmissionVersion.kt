package com.ctrlhub.core.datacapture.resource

import com.ctrlhub.core.datacapture.response.FormSchema
import com.ctrlhub.core.datacapture.response.Form
import com.ctrlhub.core.geo.Property
import com.ctrlhub.core.iam.response.User
import com.ctrlhub.core.media.response.Image
import com.ctrlhub.core.projects.operations.response.Operation
import com.ctrlhub.core.projects.schemes.response.Scheme
import com.ctrlhub.core.projects.workorders.response.WorkOrder
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.github.jasminb.jsonapi.StringIdHandler
import com.github.jasminb.jsonapi.annotations.Id
import com.github.jasminb.jsonapi.annotations.Meta
import com.github.jasminb.jsonapi.annotations.Relationship
import com.github.jasminb.jsonapi.annotations.Type
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
@Type("form-submission-versions")
class FormSubmissionVersion @JsonCreator constructor(
    @Id(StringIdHandler::class)
    var id: String = "",

    @JsonProperty("payload")
    var payload: Map<String, Any>? = null,

    @JsonProperty("iteration")
    var iteration: Int = 0,

    @Relationship("schema")
    var schema: FormSchema? = null,

    @Meta
    var meta: FormSubmissionVersionMeta? = null,

    @Relationship("author")
    var author: User? = null,

    @Relationship("form")
    var form: Form? = null,

    @Relationship("payload_images")
    var payloadImages: List<Image>? = null,

    @Relationship("payload_operations")
    var payloadOperations: List<Operation>? = null,

    @Relationship("payload_properties")
    var payloadProperties: List<Property>? = null,

    @Relationship("payload_users")
    var payloadUsers: List<User>? = null,

    @Relationship("payload_work_orders")
    var payloadWorkOrders: List<WorkOrder>? = null,

    @Relationship("payload_schemes")
    var payloadSchemes: List<Scheme>? = null,

    // raw JSON:API resource envelopes as returned in the response (kept as Map for backward compatibility)
    @JsonProperty("resources")
    var resources: List<Map<String, Any>>? = null,
) {
    val rawPayload: String?
        get() = payload?.let {
            try {
                mapper.writeValueAsString(it)
            } catch (e: Exception) {
                null
            }
        }

    // shared Jackson mapper configured to ignore unknown properties when hydrating attribute maps
    private fun resourceMapper(): ObjectMapper = mapper

    /**
     * Convert the raw resources list (List<Map<...>>) into typed JsonApiEnvelope objects.
     * This keeps the original raw structure but provides a typed view over it.
     */
    fun resourcesAsEnvelopes(): List<JsonApiEnvelope> = resources?.mapNotNull { res ->
        try {
            resourceMapper().convertValue(res, JsonApiEnvelope::class.java)
        } catch (e: Exception) {
            null
        }
    } ?: emptyList()

    /**
     * Find the full envelope for a resource by id.
     */
    fun findResourceEnvelopeById(id: String): JsonApiEnvelope? = resourcesAsEnvelopes().firstOrNull { it.data?.id == id }

    /**
     * Find the inner resource data object by id.
     */
    fun findResourceDataById(id: String): JsonApiResourceData? = findResourceEnvelopeById(id)?.data

    /**
     * Hydrate the attributes of a resource (by id) into a target class using Jackson.
     * Example: hydrateResourceAttributesById("...", Image::class.java)
     * Returns null if resource or attributes are missing or conversion fails.
     */
    fun <T> hydrateResourceAttributesById(id: String, clazz: Class<T>): T? {
        val attrs = findResourceDataById(id)?.attributes ?: return null
        return try {
            resourceMapper().convertValue(attrs, clazz)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Auto-hydrate a resource by looking up its JSON:API type and using the registered class for that type.
     * Returns the hydrated object or null if not registered or conversion fails.
     */
    fun autoHydrateById(id: String): Any? {
        val env = findResourceEnvelopeById(id) ?: return null
        val type = env.data?.type ?: return null
        val clazz = Companion.getRegisteredClass(type) ?: return null
        return hydrateResourceAttributesById(id, clazz)
    }

    /**
     * Reified convenience that attempts to auto-hydrate and cast to the expected type.
     */
    inline fun <reified T> autoHydrateByIdAs(id: String): T? {
        val any = autoHydrateById(id) ?: return null
        return any as? T
    }

    /**
     * Backwards-compatible helper: original simple lookup that returns the inner "data" map.
     */
    fun findResourceById(id: String): Map<String, Any>? {
        resources?.forEach { res ->
            val data = res["data"] as? Map<*, *> ?: return@forEach
            val dataId = data["id"] as? String
            if (dataId == id) {
                @Suppress("UNCHECKED_CAST")
                return data as Map<String, Any>
            }
        }
        return null
    }

    companion object {
        private val mapper: ObjectMapper = ObjectMapper()
            .registerModule(kotlinModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        // simple type registry mapping jsonapi resource "type" -> Class
        private val typeRegistry: MutableMap<String, Class<*>> = mutableMapOf()

        fun registerResourceType(type: String, clazz: Class<*>) {
            typeRegistry[type] = clazz
        }

        fun getRegisteredClass(type: String): Class<*>? = typeRegistry[type]
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class FormSubmissionVersionMeta(
    @JsonProperty("created_at") val createdAt: LocalDateTime? = null,
    @JsonProperty("latest") val latest: String? = null,
    @JsonProperty("is_latest") val isLatest: Boolean? = null,
)

/**
 * Lightweight typed representations for JSON:API resource envelopes and resource data.
 * These are intentionally simple (attributes are a Map) to support arbitrary resource types.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class JsonApiResourceData(
    @JsonProperty("id") val id: String? = null,
    @JsonProperty("type") val type: String? = null,
    @JsonProperty("attributes") val attributes: Map<String, Any>? = null,
    @JsonProperty("relationships") val relationships: Map<String, Any>? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class JsonApiEnvelope(
    @JsonProperty("data") val data: JsonApiResourceData? = null,
    @JsonProperty("jsonapi") val jsonapi: Map<String, Any>? = null,
)
