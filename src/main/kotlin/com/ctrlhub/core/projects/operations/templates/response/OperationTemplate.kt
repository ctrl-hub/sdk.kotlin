package com.ctrlhub.core.projects.operations.templates.response

import com.ctrlhub.core.projects.response.Label
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.jasminb.jsonapi.StringIdHandler
import com.github.jasminb.jsonapi.annotations.Id
import com.github.jasminb.jsonapi.annotations.Meta
import com.github.jasminb.jsonapi.annotations.Type
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
@Type("operation-templates")
class OperationTemplate @JsonCreator constructor(
    @Id(StringIdHandler::class) var id: String = "",
    @JsonProperty("name") var name: String = "",
    @JsonProperty("labels") var labels: List<Label> = emptyList(),
    @JsonProperty("requirements") var requirements: OperationTemplateRequirements? = null,

    @Meta
    var meta: OperationTemplateMeta = OperationTemplateMeta()
) {
    constructor(): this(
        id = "",
        requirements = null
    )
}

@JsonIgnoreProperties(ignoreUnknown = true)
class OperationTemplateRequirements @JsonCreator constructor(
    var forms: List<OperationFormResult> = emptyList()
) {
    constructor(): this(
        forms = emptyList()
    )
}

@JsonIgnoreProperties(ignoreUnknown = true)
class OperationTemplateMeta @JsonCreator constructor(
    @JsonProperty("created_at") var createdAt: LocalDateTime? = null,
    @JsonProperty("updated_at") var updatedAt: LocalDateTime? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
class OperationFormResult @JsonCreator constructor(
    var id: String = "",
    var required: Boolean = false
) {
    constructor(): this(
        id = "",
        required = false
    )
}