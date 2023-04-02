package net.phexpedition.plugins

import com.mitchellbosecke.pebble.loader.ClasspathLoader
import io.ktor.server.application.*
import io.ktor.server.pebble.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.phexpedition.authenticationClientId
import net.phexpedition.authenticationConfigurationAuthority
import net.phexpedition.authenticationRedirectUri

fun Application.configureTemplating() {
    install(Pebble) {
        loader(ClasspathLoader().apply {
            prefix = "templates"
        })
    }
    routing {
        get("/") {
            call.respond(
                PebbleContent(
                    "index.html", mapOf(
                        "authAuthority" to authenticationConfigurationAuthority.getOrThrow().value,
                        "authClientId" to authenticationClientId.getOrThrow().value,
                        "authRedirectUri" to authenticationRedirectUri.getOrThrow().value
                    )
                )
            )
        }

    }
}

