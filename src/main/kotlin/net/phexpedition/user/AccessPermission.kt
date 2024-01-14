package net.phexpedition.user


import io.smallrye.jwt.auth.principal.DefaultJWTCallerPrincipal
import jakarta.annotation.Priority
import jakarta.inject.Inject
import jakarta.ws.rs.NotFoundException
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.container.ResourceInfo
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.SecurityContext
import jakarta.ws.rs.ext.Provider
import org.jboss.logmanager.Logger
import java.lang.reflect.Method


/**
 * This enum is used to control access to web resources, for example in conjunction
 * with the [Permissions] annotation.
 */
enum class Permission {

    /**
     * Public permission means that the resource is not protected at
     * all and needs no authentication and no authorization.
     */
    PUBLIC,

    /**
     * User permission means that the user (person or technical account)
     * needs to be authenticated for that resource.
     * Further authorization checks are out of scope for this permission
     * and are subject to the implementation of a resource.
     */
    USER,

    /**
     * The resource can only be accessed for users with administrative
     * privileges.
     * Further authorization checks are out of scope for this permission
     * and are subject to the implementation of a resource.
     */
    ADMIN_USER
}


/**
 * Use this function in conjunction with [Permission] to control access
 * to web resources. A request filter such as [HttpAccessPermissionValidator]
 * can then check if a resource with given URL is protected and in what way.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Permissions(vararg val value: Permission)


/**
 * This request filter checks HTTP endpoint functions for access restrictions. For example, if
 * a HTTP function is annotated with [Permissions[Permission]], this filter checks if the
 * principal that invoked the request fulfills the required permissions by checking
 * authentication and in parts authorization.
 *
 * This is what the user principal might look like with our Google based authentication:
 * ```
 * {
 *   id='d07a80d7e098495f179abd66bc2ebf7b88407e50',
 *   name='115977854447840848693',
 *   expiration=1703256821,
 *   notBefore=1703252921,
 *   issuedAt=1703253221,
 *   issuer='https://accounts.google.com',
 *   audience=[
 *     foobarbaz.apps.googleusercontent.com
 *   ],
 *   subject='115977854447840848693',
 *   type='JWT',
 *   issuedFor='foobarbaz-foobarbaz.apps.googleusercontent.com',
 *   authTime=0,
 *   givenName='Timo',
 *   familyName='Böwing',
 *   middleName='null',
 *   nickName='null',
 *   preferredUsername='null',
 *   email='mail@example.com',
 *   emailVerified=true,
 *   allowedOrigins=null,
 *   updatedAt=0,
 *   acr='null',
 *   groups=[]
 * }
 * ```
 */
@Provider
@Priority(1)
class HttpAccessPermissionValidator(@Context val resourceInfo: ResourceInfo?) : ContainerRequestFilter {

    private val log = Logger.getLogger(this::class.qualifiedName)

    @Inject
    lateinit var userRepository: UserRepository


    override fun filter(requestContext: ContainerRequestContext?) {
        val webResource = "${requestContext?.request?.method}:${requestContext?.uriInfo?.requestUri}"
        log.info("Checking if '$webResource' requires permission")
        val callerMethod = resourceInfo!!.resourceMethod!!
        if (isPermissionRequired(callerMethod)) {
            try {
                val user = getUserFromSecurityContext(requestContext?.securityContext!!)
                if (!checkPermissions(user.permissions, getRequiredPermissions(callerMethod))) {
                    requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build())
                }
            } catch (ex: Exception) {
                log.info("Failed to call protected resource '$webResource' (mapped to ${callerMethod.name}) - user not authenticated")
                requestContext?.abortWith(Response.status(Response.Status.UNAUTHORIZED).build())
            }
        } else {
            log.info("No permission checks required for '$webResource'")
        }
    }


    private fun isPermissionRequired(caller: Method): Boolean {
        val permissionAnnotation = caller.getAnnotation(Permissions::class.java)
        return permissionAnnotation == null || permissionAnnotation.value.contains(Permission.PUBLIC)
    }


    private fun getRequiredPermissions(caller: Method): List<Permission> {
        val annotation = caller.getAnnotation(Permissions::class.java)
        if (annotation is Permissions) {
            return annotation.value.toList()
        } else {
            return listOf()
        }
    }


    private fun getUserFromSecurityContext(securityContext: SecurityContext): User {
        val principal = securityContext.userPrincipal
        if (principal is DefaultJWTCallerPrincipal) {
            val jwt: DefaultJWTCallerPrincipal = principal
            return userRepository.findUserByEmail(jwt.claim<String>("email").get())
        }
        throw NotFoundException("Could not find a user given security context - is a valid email adress maintained?")
    }


    private fun checkPermissions(offeredPermissions: List<String>, requiredPermissions: List<Permission>): Boolean {
        log.info("Permission required for resource: $requiredPermissions - offered: $requiredPermissions")
        return requiredPermissions.any { it.name in offeredPermissions }
    }

}