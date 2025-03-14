package com.ctrlhub.core.governance.response

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Label @JsonCreator constructor(
    var key: String = "",
    var value: String = ""
) {
    constructor(): this(
        key = "",
        value = ""
    )
}