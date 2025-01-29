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
class Organisation {
    @Id(StringIdHandler::class)
    lateinit var id: String
    lateinit var name: String
    var description: String? = null
    lateinit var slug: String
    lateinit var settings: Settings

    @Meta
    lateinit var meta: OrganisationMeta
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Settings {
    lateinit var nomenclature: Nomenclature
}

/**
 * Not to be confused with OrganisationResponseMeta
 */
class OrganisationMeta {
    @JsonProperty("created_at")
    @JsonDeserialize(using = JacksonLocalDateTimeDeserializer::class)
    var createdAt: LocalDateTime? = null
    lateinit var status: String
}

class OrganisationResponseMeta {
    lateinit var counts: OrganisationResponseMetaCounts
    @JsonProperty("created_at")
    @JsonDeserialize(using = JacksonLocalDateTimeDeserializer::class)
    var createdAt: LocalDateTime? = null
    lateinit var status: String
}

class OrganisationResponseMetaCounts {
    var limit: Int = 0
}

class OrganisationResponseMetaPage {
    var limit: Int = 0
    var offset: Int = 0
}

class Nomenclature {
    lateinit var governance: GovernanceNomenclature
}

class SchemesNomenclature : AbstractNomenclatureEntry()
class WorkOrdersNomenclature : AbstractNomenclatureEntry()
class OperationsNomenclature : AbstractNomenclatureEntry()

class GovernanceNomenclature {
    var schemes: List<SchemesNomenclature>? = null
    @JsonProperty("work_orders") var workOrders: List<WorkOrdersNomenclature>? = null
    var operations: List<OperationsNomenclature>? = null
}
