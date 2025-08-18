package com.ctrlhub.core.settings.timebands

import com.ctrlhub.core.Api
import com.ctrlhub.core.settings.timebands.response.TimeBand
import com.ctrlhub.core.router.Router
import com.ctrlhub.core.router.request.AbstractRequestParameters
import com.ctrlhub.core.router.request.FilterOption
import io.ktor.client.HttpClient

class TimeBandsRequestParameters(
    offset: Int? = 0,
    limit: Int? = 100,
    filterOptions: List<FilterOption> = emptyList()
) : AbstractRequestParameters(offset, limit, filterOptions)

class TimeBandsRouter(httpClient: HttpClient) : Router(httpClient) {
    /**
     * Retrieve a list of all time bands
     *
     * @return A list of all time bands
     */
    suspend fun all(
        requestParameters: TimeBandsRequestParameters = TimeBandsRequestParameters()
    ): java.util.List<TimeBand> {
        val endpoint = "/settings/time-bands"
        return fetchJsonApiResource(endpoint, requestParameters.toMap())
    }

    /**
     * Retrieve a single time band by ID
     *
     * @param timeBandId String The time band ID to retrieve
     * @return The time band with the given ID
     */
    suspend fun one(
        timeBandId: String,
        requestParameters: TimeBandsRequestParameters = TimeBandsRequestParameters()
    ): TimeBand {
        val endpoint = "/settings/time-bands/$timeBandId"
        return fetchJsonApiResource(endpoint, requestParameters.toMap(), TimeBand::class.java)
    }
}

val Api.timeBands: TimeBandsRouter
    get() = TimeBandsRouter(httpClient)
