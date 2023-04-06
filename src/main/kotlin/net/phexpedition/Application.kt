package net.phexpedition

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import net.phexpedition.plugins.*

fun main() {
    checkConfiguration()
    printConfiguration()
    embeddedServer(
        factory = Netty,
        port = webConfigurationHttpPort.getOrThrow().value,
        host = "0.0.0.0",
        module = Application::module
    )
        .start(wait = true)
}

fun Application.module() {
    configureLogging()
    //configureHTTP()
    //configureMonitoring()
    configureSecurity()
    configureTemplating()
    configureSerialization()
    configureRouting()
}
