package com.ctrlhub.core

enum class Environment {
    STAGING,
    PRODUCTION
}

object Config {
    var environment: Environment = Environment.PRODUCTION
    var userAgent: String = "CtrlHub SDK"

    val authBaseUrl: String
        get() = when (environment) {
            Environment.STAGING -> "https://auth.ctrl-hub.dev"
            Environment.PRODUCTION -> "https://auth.ctrl-hub.com"
        }

    val apiBaseUrl: String
        get() = when (environment) {
            Environment.STAGING -> "https://api.ctrl-hub.dev"
            Environment.PRODUCTION -> "https://api.ctrl-hub.com"
        }
}