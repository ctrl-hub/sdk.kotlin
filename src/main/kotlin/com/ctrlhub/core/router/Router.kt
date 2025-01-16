package com.ctrlhub.core.router

import io.ktor.client.HttpClient

abstract class Router(val httpClient: HttpClient)