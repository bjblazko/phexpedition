package net.phexpedition.user

import io.opentelemetry.instrumentation.annotations.WithSpan
import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Named
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.SecurityContext
import net.phexpedition.auth.Permission
import net.phexpedition.auth.Permissions
import org.eclipse.microprofile.jwt.JsonWebToken
import org.jboss.logmanager.Logger

@Path("/api/user")
@Named("user-api")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
class UserApi(private val userRepository: UserRepository) {

    private val log = Logger.getLogger(this::class.qualifiedName)

    @Permissions(Permission.USER)
    @GET
    @Path("_me")
    @WithSpan
    fun getSelf(@Context securityContext: SecurityContext): Response {
        val jwt = securityContext.userPrincipal as JsonWebToken
        val user = userRepository.findUserByEmail(jwt.getClaim("email"))
        return Response.ok(user).build()
    }

    @Permissions(Permission.ADMIN_USER)
    @GET
    @Path("email/{email}")
    fun getUserByEmail(@PathParam("email") email: String): Response {
        log.info("Requesting user info for email: $email")
        val user = userRepository.findUserByEmail(email)
        return Response.ok().entity(user).build()
    }

}