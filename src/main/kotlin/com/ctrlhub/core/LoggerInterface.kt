package com.ctrlhub.core

enum class LogLevel {
    INFO,
    DEBUG,
    ERROR
}

interface LoggerInterface {
    fun debug(msg: String)
    fun info(msg: String)
    fun error(msg: String)
    fun error(e: Exception)
}