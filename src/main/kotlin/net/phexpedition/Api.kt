package net.phexpedition

import jakarta.enterprise.context.RequestScoped
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.SecurityContext
import net.phexpedition.user.userFromSecurityContext
import java.util.*


data class Person(val id: String = UUID.randomUUID().toString(), val jwt: String)

@Path("/api2")
@RequestScoped
class Api() {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun api(@Context ctx: SecurityContext): Person {
        val user = userFromSecurityContext(ctx)
        println("User: ${user.displayName}")
        return Person(jwt = ctx.userPrincipal.name)
    }

    fun fromRepo() {

    }
}