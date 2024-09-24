package skeleton.backend.enums

enum class UserTypeEnum(
    val idType: String,
    // TODO: This enum is for additional data saving enum. use it on your demands!
) {
    PERSONAL("REG_NO",),
    CORPORATE("BUSINESS_NO",);

    companion object {
        fun fromIdType(idType: String): UserTypeEnum = entries.find { it.idType == idType }!!
    }
}