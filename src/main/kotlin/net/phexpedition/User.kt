package net.phexpedition

import jakarta.ws.rs.core.SecurityContext
import org.eclipse.microprofile.jwt.JsonWebToken

data class User(
    val id: String,
    val email: String,
    val displayName: String
)

fun userFromSecurityContext(ctx: SecurityContext): User {
    val jwt = ctx.userPrincipal as JsonWebToken
    return User(
        id = "123",//jwt.getClaim("id"),
        email = jwt.getClaim("email"),
        displayName = "${jwt.claim<String>("givenName")} ${jwt.claim<String>("familyName")}"
    )
}