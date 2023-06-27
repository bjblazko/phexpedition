package net.phexpedition.user

import jakarta.enterprise.context.RequestScoped
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

@Path("/api/user")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
class UserApi {

    @GET
    fun getUserById(): Response {
        return Response.ok().build()
    }
}