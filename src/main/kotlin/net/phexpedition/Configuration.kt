package net.phexpedition

import java.util.Optional

data class ConfigurationValue<T>(
    val value: T,
    val key: String,
    val fromDefault: Boolean
)

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
    webConfigurationHttpPort.getOrThrow()
    authenticationConfigurationAuthority.getOrThrow()
    authenticationRedirectUri.getOrThrow()
    authenticationClientId.getOrThrow()
    authenticationJwkProviderUri.getOrThrow()
    authenticationRealm.getOrThrow()
    authenticationIssuer.getOrThrow()
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
