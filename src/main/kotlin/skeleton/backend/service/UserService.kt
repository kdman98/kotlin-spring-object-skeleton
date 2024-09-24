package skeleton.backend.service

import skeleton.backend.config.BadRequestException
import skeleton.backend.exposed.domain.User
import skeleton.backend.repository.UserRepository
import skeleton.backend.util.JwtUtil

object UserService {
    private val userRepository by lazy { UserRepository }

    fun signup(user: User) {
        if (!user.userId.contains('@')) {
            throw BadRequestException("bad email")
        }
        val hashedPassword = skeleton.backend.util.EncryptionUtil.hashPassword(user.password)
        val encryptedIdValue = skeleton.backend.util.EncryptionUtil.encrypt(user.idValue)

        val userToSave = user.copy(
            password = hashedPassword,
            idValue = encryptedIdValue
        )

        userRepository.createUser(userToSave)
    }

    fun processLoginAndReturnToken(userId: String, rawPassword: String): String {
        val user = userRepository.findUserByUserId(userId)
        if (skeleton.backend.util.EncryptionUtil.verifyPassword(rawPassword, user.password)) {
            return JwtUtil.generateToken(userId)
        } else {
            throw BadRequestException("wrong parameter")
        }
    }

}
