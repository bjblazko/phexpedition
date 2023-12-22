package net.phexpedition.user

data class User(
    val id: String,
    val email: String,
    val displayName: String,
    val permissions: List<String> = listOf()
)