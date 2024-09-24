package skeleton.backend.exposed.table

import org.jetbrains.exposed.sql.Table

object UsersTable : Table("users") {
    val userId = varchar("user_id", 255)
    val password = varchar("password", 255)
    val user_name = varchar("user_name", 255)
    val idType = varchar("id_type", 50)
    val idValue = varchar("id_value", 50)

    override val primaryKey = PrimaryKey(userId)
}
