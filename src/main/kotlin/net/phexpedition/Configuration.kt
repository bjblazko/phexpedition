package net.phexpedition

import java.util.Optional
import io.ktor.server.application.*


data class ConfigurationValue<T>(
    val value: T,
    val key: String,
    val fromDefault: Boolean
) {
    override fun toString(): String {
        return "$key=$value"
    }
}

val webConfigurationHttpPort = getIntConfigurationFromEnvironment("WEB_HTTP_PORT", Optional.of(8080))

val authenticationConfigurationAuthority = getStringConfigurationFromEnvironment(
    "AUTH_AUTHORITY",
    Optional.of("https://accounts.google.com/o/oauth2/v2/auth")
)
val authenticationRedirectUri = getStringConfigurationFromEnvironment(
    key = "AUTH_REDIRECT_URI",
    defaultValue = Optional.of("http://localhost:8080")
)
val authenticationClientId = getStringConfigurationFromEnvironment(
    key = "AUTH_CLIENT_ID",
    defaultValue = Optional.empty()
)
val authenticationJwkProviderUri = getStringConfigurationFromEnvironment(
    key = "AUTH_JWK_PROVIDER_URI",
    defaultValue = Optional.of("https://www.googleapis.com/oauth2/v3/certs")
)
val authenticationRealm = getStringConfigurationFromEnvironment(
    key = "AUTH_REALM",
    defaultValue = Optional.of("phexpedition")
)
val authenticationIssuer = getStringConfigurationFromEnvironment(
    key = "AUTH_ISSUER",
    defaultValue = Optional.of("https://accounts.google.com")
)


private fun getIntConfigurationFromEnvironment(
    key: String,
    defaultValue: Optional<Int>
): Result<ConfigurationValue<Int>> {
    return getConfigurationFromEnvironment(key, defaultValue) { it.toInt() }
}

fun getStringConfigurationFromEnvironment(
    key: String,
    defaultValue: Optional<String>
): Result<ConfigurationValue<String>> {
    return getConfigurationFromEnvironment(key, defaultValue) { it }
}

fun getBooleanConfigurationFromEnvironment(
    key: String,
    defaultValue: Optional<Boolean>
): Result<ConfigurationValue<Boolean>> {
    return getConfigurationFromEnvironment(key, defaultValue) { it.toBoolean() }
}


fun checkConfiguration() {
    val exceptions = mutableListOf<Throwable>()
    webConfigurationHttpPort.onFailure { ex -> exceptions.add(ex) }
    authenticationConfigurationAuthority.onFailure { ex -> exceptions.add(ex) }
    authenticationRedirectUri.onFailure { ex -> exceptions.add(ex) }
    authenticationClientId.onFailure { ex -> exceptions.add(ex) }
    authenticationJwkProviderUri.onFailure { ex -> exceptions.add(ex) }
    authenticationRealm.onFailure { ex -> exceptions.add(ex) }
    authenticationIssuer.onFailure { ex -> exceptions.add(ex) }

    if (exceptions.isNotEmpty()) {
        var message = "Failed to start due to missing mandatory ENV variables: "
        for (ex in exceptions) {
            message += "${ex.message}, "
        }
        throw IllegalArgumentException(message)
    }
}

fun printConfiguration() {
    println(webConfigurationHttpPort.getOrThrow())
    println(authenticationConfigurationAuthority.getOrThrow())
    println(authenticationRedirectUri.getOrThrow())
    println(authenticationClientId.getOrThrow())
    println(authenticationJwkProviderUri.getOrThrow())
    println(authenticationRealm.getOrThrow())
    println(authenticationIssuer.getOrThrow())
}

private inline fun <reified T> getConfigurationFromEnvironment(
    key: String,
    defaultValue: Optional<T>,
    converter: (String) -> T
): Result<ConfigurationValue<T>> {
    if (defaultValue.isPresent) {
        val stringValue = System.getenv(key) ?: defaultValue.get().toString()
        val value = converter(stringValue)
        return Result.success(ConfigurationValue(value, key, fromDefault = System.getenv(key) == null))
    }
    if (defaultValue.isEmpty && System.getenv(key) != null) {
        val stringValue = System.getenv(key)
        val value = converter(stringValue)
        return Result.success(ConfigurationValue(value, key, fromDefault = false))
    }
    return Result.failure(
        IllegalArgumentException(
            "Failed to get configuration for environment $key but it was declared mandatory with no defaults"
        )
    )
}
