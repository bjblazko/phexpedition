package net.phexpedition.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Person(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "[?]",
)

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }

    val personList = listOf(
        Person(name = "John Doe"),
        Person(name = "Jane Doe")
    )

    routing {
        get("/api") {
            call.respond(personList)
        }
        authenticate(JWKS) {
            get("/api2") {
                val principal = call.principal<JWTPrincipal>()

                println(">>> Hello, " + principal!!.getClaim("email", String::class))
                call.respond(personList)
            }
        }
    }

}
