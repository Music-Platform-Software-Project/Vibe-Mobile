package model

data class RegisterPayload(
    val email: String,
    val password: String,
    val username: String
)