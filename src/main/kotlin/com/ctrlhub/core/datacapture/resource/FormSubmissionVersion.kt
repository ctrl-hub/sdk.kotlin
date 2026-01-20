package com.ctrlhub.core.datacapture.resource

import com.ctrlhub.core.datacapture.response.Form
import com.ctrlhub.core.datacapture.response.FormSchema
import com.ctrlhub.core.datacapture.response.FormSubmission
import com.ctrlhub.core.geo.Property
import com.ctrlhub.core.iam.response.User
import com.ctrlhub.core.json.JsonConfig
import com.ctrlhub.core.media.response.Image
import com.ctrlhub.core.projects.operations.response.Operation
import com.ctrlhub.core.projects.response.Organisation
import com.ctrlhub.core.projects.schemes.response.Scheme
import com.ctrlhub.core.projects.workorders.response.WorkOrder
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.jasminb.jsonapi.StringIdHandler
import com.github.jasminb.jsonapi.ResourceConverter
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

    @Relationship("submission")
    var submission: FormSubmission? = null,

    @Relationship("organisation")
    var organisation: Organisation? = null,

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
                resourceMapper().writeValueAsString(it)
            } catch (e: Exception) {
                null
            }
        }

    // shared Jackson mapper configured to ignore unknown properties when hydrating attribute maps
    private fun resourceMapper(): ObjectMapper = JsonConfig.getMapper()

    /**
     * Create a configured Jasminb ResourceConverter for the given target class and included classes.
     * Also include any classes registered in the companion type registry so the converter knows
     * about all registered resource types.
     */
    private fun resourceConverter(targetClass: Class<*>, vararg includedClasses: Class<*>): ResourceConverter {
        // Use the registered classes plus the explicit target and included classes.
        val registryClasses: List<Class<*>> = companionTypeRegistryClasses()

        val classesList: MutableList<Class<*>> = ArrayList()
        classesList.add(targetClass)
        // add registry classes (skip duplicates)
        for (c in registryClasses) {
            if (!classesList.contains(c)) classesList.add(c)
        }
        // add explicitly passed included classes
        for (c in includedClasses) {
            if (!classesList.contains(c)) classesList.add(c)
        }

        val classesArray: Array<Class<*>> = classesList.toTypedArray()
        val rc = ResourceConverter(resourceMapper(), *classesArray)
        try {
            rc.enableSerializationOption(com.github.jasminb.jsonapi.SerializationFeature.INCLUDE_RELATIONSHIP_ATTRIBUTES)
        } catch (_: Throwable) {
            // ignore
        }
        return rc
    }

    // Helper to access companion's registry as a list (keeps the converter creation tidy)
    private fun companionTypeRegistryClasses(): List<Class<*>> = synchronized(Companion) {
        getAllRegisteredClasses()
    }

    /**
     * Find the raw JSON:API envelope Map for a resource by id.
     */
    private fun findRawResourceEnvelopeById(id: String): Map<String, Any>? {
        resources?.forEach { res ->
            val data = res["data"] as? Map<*, *> ?: return@forEach
            val dataId = data["id"] as? String
            if (dataId == id) {
                @Suppress("UNCHECKED_CAST")
                return res
            }
        }
        return null
    }

    /**
     * Hydrate a resource by using Jasminb's ResourceConverter. Serialises the raw envelope Map
     * to bytes using the configured ObjectMapper and passes it to ResourceConverter.readDocument.
     */
    private fun <T> hydrateResourceUsingConverter(id: String, clazz: Class<T>, vararg includedClasses: Class<*>): T? {
        val envelope = findRawResourceEnvelopeById(id) ?: return null

        return try {
            val bytes = resourceMapper().writeValueAsBytes(envelope)
            val rc = resourceConverter(clazz, *includedClasses)
            val document = rc.readDocument(bytes, clazz)
            document.get()
        } catch (e: Exception) {
            throw e
        }
    }

    /**
     * Hydrate a resource (by id) into a target class using Jasminb ResourceConverter.
     * This is the primary public method for hydration.
     */
    fun <T> hydrateResourceById(id: String, clazz: Class<T>): T? {
        return hydrateResourceUsingConverter(id, clazz)
    }

    /**
     * Auto-hydrate a resource by looking up its JSON:API type and using the registered class for that type.
     */
    fun autoHydrateById(id: String): Any? {
        val envelope = findRawResourceEnvelopeById(id) ?: return null

        val data = envelope["data"] as? Map<*, *> ?: return null
        val type = data["type"] as? String ?: return null
        val clazz = getRegisteredClass(type) ?: return null
        return hydrateResourceUsingConverter(id, clazz)
    }

    companion object {
        // simple type registry mapping jsonapi resource "type" -> Class
        private val typeRegistry: MutableMap<String, Class<*>> = mutableMapOf()

        fun registerResourceType(type: String, clazz: Class<*>) {
            typeRegistry[type] = clazz
        }

        fun getRegisteredClass(type: String): Class<*>? = typeRegistry[type]

        // expose a synchronized way to get the registry contents as a List
        fun getAllRegisteredClasses(): List<Class<*>> = synchronized(typeRegistry) {
            typeRegistry.values.toList()
        }
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class FormSubmissionVersionMeta(
    @JsonProperty("created_at") val createdAt: LocalDateTime? = null,
    @JsonProperty("latest") val latest: String? = null,
    @JsonProperty("is_latest") val isLatest: Boolean? = null,
)
