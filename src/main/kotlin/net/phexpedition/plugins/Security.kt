package net.phexpedition.plugins

import com.auth0.jwk.JwkProviderBuilder
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable
import net.phexpedition.authenticationIssuer
import net.phexpedition.authenticationJwkProviderUri
import net.phexpedition.authenticationRealm
import java.net.URL
import java.util.concurrent.TimeUnit

const val JWKS = "jwks"

@Serializable
data class AuthenticationFailed(
    val httpStatus: Int,
    val message: String
)

fun Application.configureSecurity() {

    val jwkProvider = JwkProviderBuilder(URL(authenticationJwkProviderUri.getOrThrow().value))
        .cached(10, 24, TimeUnit.HOURS)
        .rateLimited(10, 1, TimeUnit.MINUTES)
        .build()
    val issuer = authenticationIssuer.getOrThrow().value

    install(Authentication) {
        jwt(JWKS) {
            realm = authenticationRealm.getOrThrow().value
            verifier(jwkProvider, issuer) {
                acceptLeeway(3)
            }

            // Validate Token
            validate { credential ->
                if (credential.payload.getClaim("email").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }

            // Handle error
            challenge { _, _ ->
                val httpStatus = HttpStatusCode.Unauthorized
                call.respond(
                    status = httpStatus,
                    message = AuthenticationFailed(
                        httpStatus.value,
                        message = "JWKS failed"
                    )
                )
            }
        }
    }

}


