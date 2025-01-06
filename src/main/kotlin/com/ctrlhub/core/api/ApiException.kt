package com.ctrlhub.core.api

// TODO - flesh this out more with different types of exceptions
class ApiException(message: String, e: Throwable): Exception(message, e)