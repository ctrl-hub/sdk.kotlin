package com.ctrlhub.core.iam.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.jasminb.jsonapi.StringIdHandler
import com.github.jasminb.jsonapi.annotations.Id
import com.github.jasminb.jsonapi.annotations.Type

@Type("users")
class User {
    @Id(StringIdHandler::class)
    lateinit var id: String
    lateinit var email: String
    lateinit var profile: Profile
    lateinit var identities: List<Identity>
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
    lateinit var address: ProfileAddress
    lateinit var contact: ProfileContact
    lateinit var personal: ProfilePersonal
    lateinit var settings: ProfileSettings
    lateinit var work: ProfileWork
}

class ProfileAddress {
    lateinit var area: String
    @JsonProperty("country_code") lateinit var countryCode: String
    lateinit var county: String
    lateinit var name: String
    lateinit var number: String
    lateinit var postcode: String
    lateinit var street: String
    lateinit var town: String
    lateinit var what3words: String
}

class ProfileContact {
    lateinit var landline: String
    lateinit var mobile: String
}

@JsonIgnoreProperties(ignoreUnknown = true)
class ProfilePersonal {
    lateinit var dob: String
    @JsonProperty("first_name") lateinit var firstName: String
    @JsonProperty("last_name") lateinit var lastName: String
}

class ProfileSettings {
    @JsonProperty("preferred_language") lateinit var preferredLanguage: String
    lateinit var timezone: String
}

class ProfileWork {
    lateinit var cscs: String
    lateinit var eusr: String
    lateinit var occupation: String
    @JsonProperty("start_date") lateinit var startDate: String
}