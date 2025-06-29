package com.ctrlhub.core

import com.ctrlhub.BuildConfig

enum class Environment {
    STAGING,
    PRODUCTION
}

object Config {
    var environment: Environment = Environment.PRODUCTION
    var userAgent: String = "CtrlHub SDK"
    var version: String = BuildConfig.VERSION_NAME

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