package skeleton.backend.repository

import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import skeleton.backend.enums.UserTypeEnum
import skeleton.backend.exposed.domain.User
import skeleton.backend.exposed.table.UsersTable

object UserRepository {
    fun createUser(user: User) {
        return transaction {
            UsersTable.insert {
                it[userId] = user.userId
                it[password] = user.password
                it[user_name] = user.name
                it[idType] = user.userType.idType
                it[idValue] = user.idValue
            }
        }
    }

    fun findUserByUserId(userId: String): User {
        return transaction {
            UsersTable.select(
                UsersTable.columns
            ).where {
                (UsersTable.userId eq userId)
            }.mapNotNull {
                User(
                    userId = it[UsersTable.userId],
                    password = it[UsersTable.password],
                    name = it[UsersTable.user_name],
                    userType = UserTypeEnum.fromIdType(it[UsersTable.idType]),
                    idValue = it[UsersTable.idValue]
                )
            }.single()
        }
    }

    fun findUserByIdAndPassword(userId: String, password: String): User? {
        return transaction {
            UsersTable.select(
                UsersTable.columns
            ).where {
                (UsersTable.userId eq userId) and
                        (UsersTable.password eq password)
            }.mapNotNull {
                User(
                    userId = it[UsersTable.userId],
                    password = it[UsersTable.password],
                    name = it[UsersTable.user_name],
                    userType = UserTypeEnum.fromIdType(it[UsersTable.idType]),
                    idValue = it[UsersTable.idValue]
                )
            }.singleOrNull()
        }
    }
}
