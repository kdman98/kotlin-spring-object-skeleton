package skeleton.backend.api

data class SignupRequest(
    val userId: String,
    val password: String,
    val name: String,
    val idType: String,
    val idValue: String,
)