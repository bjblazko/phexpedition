package net.phexpedition

import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerResponseContext
import jakarta.ws.rs.container.ContainerResponseFilter
import jakarta.ws.rs.ext.Provider
import java.util.UUID

public val nonce = "nonce-${UUID.randomUUID().toString()}"

class App {
}

@Provider
class HttpResponseFilter : ContainerResponseFilter {
    override fun filter(requestContext: ContainerRequestContext?, responseContext: ContainerResponseContext?) {
        responseContext!!.headers.add("X-Frame-Options", "SAMEORIGIN")
        responseContext.headers.add("Strict-Transport-Security", "max-age=63072000; includeSubDomains; preload")
        responseContext.headers.add("X-Content-Type-Options", "nosniff")
        responseContext.headers.add("Content-Security-Policy", "script-src '${nonce}' 'strict-dynamic';")
        responseContext.headers.add("X-XSS-Protection", "1; mode=block")
    }

}