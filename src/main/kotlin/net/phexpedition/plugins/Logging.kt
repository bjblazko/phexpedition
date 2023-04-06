package net.phexpedition.plugins

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.util.ContextInitializer
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import org.slf4j.LoggerFactory
import org.slf4j.event.Level


fun Application.configureLogging() {
    val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
    loggerContext.reset()

    val contextInitializer = ContextInitializer(loggerContext)
    contextInitializer.autoConfig()

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    // ...
}
