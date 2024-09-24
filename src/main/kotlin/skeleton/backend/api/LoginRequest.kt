package skeleton.backend.api

data class LoginRequest(
    val userId: String,
    val password: String,
)
