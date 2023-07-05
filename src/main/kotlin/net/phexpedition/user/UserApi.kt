package net.phexpedition.user

import jakarta.enterprise.context.RequestScoped
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.SecurityContext

@Path("/api/user")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
class UserApi {

    @Path("_me")
    @GET
    fun getSelf(@Context ctx: SecurityContext): Response {
        println("---> getSelf")
        val user = userFromSecurityContext(ctx)
        println("---> user $user")
        val ube = findByEMail("foo@example.com")//user.email)
        println("---> ube $ube")
        return Response.ok(ube).build()
    }

    @GET
    fun getUserById(): Response {

        return Response.ok().build()
    }
}