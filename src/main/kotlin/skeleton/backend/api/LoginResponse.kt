package skeleton.backend.api

data class LoginResponse(
    val resultCode: Int,
    val resultMsg: String,
    val token: String,
)
