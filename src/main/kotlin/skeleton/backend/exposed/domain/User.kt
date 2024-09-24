package skeleton.backend.exposed.domain

import skeleton.backend.enums.UserTypeEnum

data class User(
    val userId: String,
    val password: String,
    val name: String,
    val userType: UserTypeEnum,
    val idValue: String
)
