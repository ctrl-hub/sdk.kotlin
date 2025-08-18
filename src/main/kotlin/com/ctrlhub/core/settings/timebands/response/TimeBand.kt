package com.ctrlhub.core.settings.timebands.response

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.jasminb.jsonapi.annotations.Id
import com.github.jasminb.jsonapi.annotations.Meta
import com.github.jasminb.jsonapi.annotations.Type

@Type("time-bands")
@JsonIgnoreProperties(ignoreUnknown = true)
data class TimeBand @JsonCreator constructor(
    @Id var id: String = "",
    @JsonProperty("end") var end: String = "",
    @JsonProperty("name") var name: String = "",
    @JsonProperty("start") var start: String = "",
    @JsonProperty("meta") @Meta var meta: TimeBandMeta? = null
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class TimeBandMeta @JsonCreator constructor(
        @JsonProperty("created_at") var createdAt: String = "",
        @JsonProperty("updated_at") var updatedAt: String = "",
        @JsonProperty("modified_at") var modifiedAt: String = ""
    )
}
