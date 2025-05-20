package com.ctrlhub.core.governance.response

import com.ctrlhub.core.AbstractNomenclatureEntry
import com.ctrlhub.core.serializer.JacksonLocalDateTimeDeserializer
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.github.jasminb.jsonapi.StringIdHandler
import com.github.jasminb.jsonapi.annotations.Id
import com.github.jasminb.jsonapi.annotations.Meta
import com.github.jasminb.jsonapi.annotations.Type
import java.time.LocalDateTime

class OrganisationCollection {
    lateinit var meta: OrganisationResponseMeta
    lateinit var items: List<Organisation>
}

@Type("organisations")
@JsonIgnoreProperties(ignoreUnknown = true)
class Organisation {
    @Id(StringIdHandler::class)
    lateinit var id: String
    lateinit var name: String
    var description: String? = null
    lateinit var slug: String
    var settings: Settings? = null

    @Meta
    lateinit var meta: OrganisationMeta
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Settings {
    var nomenclature: Nomenclature? = null
}

/**
 * Not to be confused with OrganisationResponseMeta
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class OrganisationMeta {
    @JsonProperty("created_at")
    @JsonDeserialize(using = JacksonLocalDateTimeDeserializer::class)
    var createdAt: LocalDateTime? = null
    lateinit var status: String
}

@JsonIgnoreProperties(ignoreUnknown = true)
class OrganisationResponseMeta {
    lateinit var counts: OrganisationResponseMetaCounts
    @JsonProperty("created_at")
    @JsonDeserialize(using = JacksonLocalDateTimeDeserializer::class)
    var createdAt: LocalDateTime? = null
    lateinit var status: String
}

@JsonIgnoreProperties(ignoreUnknown = true)
class OrganisationResponseMetaCounts {
    var limit: Int = 0
}

@JsonIgnoreProperties(ignoreUnknown = true)
class OrganisationResponseMetaPage {
    var limit: Int = 0
    var offset: Int = 0
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Nomenclature {
    var governance: GovernanceNomenclature? = null
}

@JsonIgnoreProperties(ignoreUnknown = true)
class SchemesNomenclature : AbstractNomenclatureEntry()
@JsonIgnoreProperties(ignoreUnknown = true)
class WorkOrdersNomenclature : AbstractNomenclatureEntry()
@JsonIgnoreProperties(ignoreUnknown = true)
class OperationsNomenclature : AbstractNomenclatureEntry()
@JsonIgnoreProperties(ignoreUnknown = true)
class GovernanceNomenclature {
    var schemes: List<SchemesNomenclature>? = null
    @JsonProperty("work_orders") var workOrders: List<WorkOrdersNomenclature>? = null
    var operations: List<OperationsNomenclature>? = null
}
