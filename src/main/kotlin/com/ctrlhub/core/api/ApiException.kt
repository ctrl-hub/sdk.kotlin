package com.ctrlhub.core.api

class ApiException(message: String, e: Throwable): Exception(message, e) {
}