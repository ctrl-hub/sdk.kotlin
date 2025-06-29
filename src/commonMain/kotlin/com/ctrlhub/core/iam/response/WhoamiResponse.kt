package com.ctrlhub.core.iam.response

import com.ctrlhub.core.api.Assignable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.jasminb.jsonapi.StringIdHandler
import com.github.jasminb.jsonapi.annotations.Id
import com.github.jasminb.jsonapi.annotations.Type

@Type("users")
class User : Assignable {
    @Id(StringIdHandler::class)
    override var id: String? = null
    var email: String? = null
    var profile: Profile? = null
    var identities: List<Identity> = emptyList()
}

class Identity {
    val platform: String? = null
    val id: String? = null
    val meta: IdentityMeta? = null
}

@JsonIgnoreProperties(ignoreUnknown = true)
class IdentityMeta {
    @JsonProperty("organisation_id") val organisationId: String? = null
}

class Profile {
    var address: ProfileAddress? = null
    var contact: ProfileContact? = null
    var personal: ProfilePersonal? = null
    var settings: ProfileSettings? = null
    var work: ProfileWork? = null
}

class ProfileAddress {
    var area: String? = ""
    @JsonProperty("country_code") var countryCode: String? = ""
    var county: String? = ""
    var name: String? = ""
    var number: String? = ""
    var postcode: String? = ""
    var street: String? = ""
    var town: String? = ""
    var what3words: String? = ""
}

class ProfileContact {
    var landline: String? = ""
    var mobile: String? = ""
}

@JsonIgnoreProperties(ignoreUnknown = true)
class ProfilePersonal {
    var dob: String? = ""
    @JsonProperty("first_name") var firstName: String? = ""
    @JsonProperty("last_name") var lastName: String? = ""
}

class ProfileSettings {
    @JsonProperty("preferred_language") var preferredLanguage: String? = ""
    var timezone: String? = ""
}

class ProfileWork {
    var cscs: String? = null
    var eusr: String? = null
    var occupation: String? = null
    @JsonProperty("start_date") var startDate: String? = null
}