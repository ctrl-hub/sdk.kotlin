package com.ctrlhub.core

class DefaultLogger(
    private val currentLogLevel: LogLevel
): LoggerInterface {
    override fun debug(msg: String) {
        if (currentLogLevel != LogLevel.DEBUG) {
            return
        }

        println("[CtrlHub] [DEBUG] $msg")
    }

    override fun info(msg: String) {
        if (currentLogLevel != LogLevel.INFO) {
            return
        }

        println("[CtrlHub] [INFO] $msg")
    }

    override fun error(msg: String) {
        if (currentLogLevel != LogLevel.ERROR) {
            return
        }

        println("[CtrlHub] [ERROR} $msg")
    }

    override fun error(e: Exception) {
        error(e.stackTrace.toString())
    }
}