package net.phexpedition.auth


/**
 * Use this function in conjunction with [Permission] to control access
 * to web resources. A request filter such as [HttpAccessPermissionValidator]
 * can then check if a resource with given URL is protected and in what way.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Permissions(vararg val value: Permission)


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
