package com.ctrlhub.core

enum class Environment {
    STAGING,
    PRODUCTION
}

object Config {
    var environment: Environment = Environment.PRODUCTION

    val authBaseUrl: String
        get() = when (environment) {
            Environment.STAGING -> "auth.ctrl-hub.dev"
            Environment.PRODUCTION -> "auth.ctrl-hub.com"
        }

    val apiBaseUrl: String
        get() = when (environment) {
            Environment.STAGING -> "api.ctrl-hub.dev"
            Environment.PRODUCTION -> "api.ctrl-hub.com"
        }
}