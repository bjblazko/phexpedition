package net.phexpedition

import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerResponseContext
import jakarta.ws.rs.container.ContainerResponseFilter
import jakarta.ws.rs.ext.Provider
import java.util.*
import net.phexpedition.user.HttpAccessPermissionValidator

val nonce = UUID.randomUUID().toString()

/**
 * Entry points to this application can be found in according `~Api` classes in according packages.
 * Please note that requests and responses are subject to evaluation of one or more filters, such as
 * [HttpResponseFilter] or  [HttpAccessPermissionValidator].
 */
class App {
}


/**
 * Generic HTTP response filter that is involved for EVERY HTTP response with the task of setting
 * generic HTTP headers that shall always be present, e.g. for security reasons.
 */
@Provider
class HttpResponseFilter : ContainerResponseFilter {
    override fun filter(requestContext: ContainerRequestContext?, responseContext: ContainerResponseContext?) {
        responseContext!!.headers.add("X-Frame-Options", "SAMEORIGIN")
        responseContext.headers.add("Strict-Transport-Security", "max-age=63072000; includeSubDomains; preload")
        responseContext.headers.add("X-Content-Type-Options", "nosniff")
        responseContext.headers.add("Content-Security-Policy", "script-src 'nonce-${nonce}';")
        responseContext.headers.add("X-XSS-Protection", "1; mode=block")
    }

}