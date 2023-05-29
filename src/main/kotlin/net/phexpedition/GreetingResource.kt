package net.phexpedition

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import java.time.Instant
import java.util.UUID

data class HelloJson(
    val id: String = UUID.randomUUID().toString(),
    val time: Instant = Instant.now()
)

@Path("/hello")
class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun hello() = "Hello RESTEasy"

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun helloJson() = HelloJson()

}