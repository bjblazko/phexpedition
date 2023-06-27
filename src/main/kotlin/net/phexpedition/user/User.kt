package net.phexpedition.user

import com.google.cloud.firestore.Firestore
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
        displayName = "${jwt.claim<String>("given_name").get()} ${jwt.claim<String>("family_name").get()}"
    )
}
