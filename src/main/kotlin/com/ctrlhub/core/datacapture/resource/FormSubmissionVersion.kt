package com.ctrlhub.core.datacapture.resource

import com.ctrlhub.core.datacapture.response.FormSchema
import com.ctrlhub.core.datacapture.response.Form
import com.ctrlhub.core.geo.Property
import com.ctrlhub.core.iam.response.User
import com.ctrlhub.core.media.response.Image
import com.ctrlhub.core.projects.operations.response.Operation
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.jasminb.jsonapi.StringIdHandler
import com.github.jasminb.jsonapi.annotations.Id
import com.github.jasminb.jsonapi.annotations.Relationship
import com.github.jasminb.jsonapi.annotations.Type

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

    @JsonProperty("meta")
    var meta: Map<String, Any>? = null,

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
)
