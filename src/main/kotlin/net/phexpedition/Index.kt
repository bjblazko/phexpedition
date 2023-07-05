package net.phexpedition

import io.quarkus.qute.Location
import io.quarkus.qute.Template
import io.quarkus.qute.TemplateInstance
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.util.UUID

@Path("/")
class Index(

    @Location("index")
    val template: Template,

    @ConfigProperty(name = "phex.auth.redirect-url", defaultValue = "http://localhost:8080")
    val baseUrl: String,

    @ConfigProperty(name = "phex.auth.authority", defaultValue = "https://accounts.google.com/o/oauth2/v2/auth")
    val authority: String,

    @ConfigProperty(name = "quarkus.oidc.client-id")
    val clientId: String

) {

    @GET
    @Produces(MediaType.TEXT_HTML)
    fun index(): TemplateInstance {
        return template
            .data("authRedirectUri", baseUrl)
            .data("baseUrl", baseUrl)
            .data("authClientId", clientId)
            .data("authAuthority", authority)
            .data("nonce", nonce)
    }

}