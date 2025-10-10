package com.ctrlhub.core.datacapture.response

import com.ctrlhub.core.datacapture.resource.FormSubmissionVersion
import com.ctrlhub.core.projects.response.Organisation
import com.ctrlhub.core.iam.response.User
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.jasminb.jsonapi.StringIdHandler
import com.github.jasminb.jsonapi.annotations.Id
import com.github.jasminb.jsonapi.annotations.Meta
import com.github.jasminb.jsonapi.annotations.Relationship
import com.github.jasminb.jsonapi.annotations.Type
import java.time.LocalDateTime

@Type("form-submissions")
@JsonIgnoreProperties(ignoreUnknown = true)
class FormSubmission @JsonCreator constructor(
    @Id(StringIdHandler::class) var id: String = "",

    @Relationship("contributors")
    var contributors: java.util.List<User>? = null,

    @Relationship("creator")
    var creator: User? = null,

    @Relationship("form")
    var form: Form? = null,

    @Relationship("organisation")
    var organisation: Organisation? = null,

    @Relationship("versions")
    var versions: java.util.List<FormSubmissionVersion>? = null,

    @Meta
    var meta: FormSubmissionMeta? = null
) {
    constructor(): this(id = "")
}

@JsonIgnoreProperties(ignoreUnknown = true)
class FormSubmissionMeta @JsonCreator constructor(
    @JsonProperty("created_at") var createdAt: LocalDateTime? = null,
    @JsonProperty("modified_at") var modifiedAt: LocalDateTime? = null,
    @JsonProperty("counts") var counts: FormSubmissionCounts? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class FormSubmissionCounts(
    @JsonProperty("versions") val versions: Int = 0,
    @JsonProperty("contributors") val contributors: Int = 0
)
