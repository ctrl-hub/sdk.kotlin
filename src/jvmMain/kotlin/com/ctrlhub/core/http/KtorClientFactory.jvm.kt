package com.ctrlhub.core.http

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO

actual fun getHttpEngine(): HttpClientEngineFactory<*> {
    return CIO
}