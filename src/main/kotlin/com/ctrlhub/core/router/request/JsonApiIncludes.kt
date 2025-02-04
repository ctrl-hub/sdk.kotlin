package com.ctrlhub.core.router.request

/**
 * A marker interface for includes options that can be specified
 * when making a request
 */
interface JsonApiIncludes {
    fun value(): String
}