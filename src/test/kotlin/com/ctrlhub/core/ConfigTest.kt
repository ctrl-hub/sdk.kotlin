package com.ctrlhub.core

import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ConfigTest {
    @AfterTest
    fun tearDown() {
        // Reset the environment to its default after each test
        Config.environment = Environment.PRODUCTION
    }

    @Test
    fun `authBaseUrl should return correct value for PRODUCTION`() {
        Config.environment = Environment.PRODUCTION

        assertEquals("https://auth.ctrl-hub.com", Config.authBaseUrl)
    }

    @Test
    fun `authBaseUrl should return correct value for STAGING`() {
        Config.environment = Environment.STAGING

        assertEquals("https://auth.ctrl-hub.dev", Config.authBaseUrl)
    }

    @Test
    fun `apiBaseUrl should return correct value for PRODUCTION`() {
        Config.environment = Environment.PRODUCTION

        assertEquals("https://api.ctrl-hub.com", Config.apiBaseUrl)
    }

    @Test
    fun `apiBaseUrl should return correct value for STAGING`() {
        Config.environment = Environment.STAGING

        assertEquals("https://api.ctrl-hub.dev", Config.apiBaseUrl)
    }
}