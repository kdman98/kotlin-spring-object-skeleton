package skeleton.backend.util

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.security.Key
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object EncryptionUtil {

    private val bcryptEncoder = BCryptPasswordEncoder()
    private const val SECRET_KEY = "and One Went Two" // TODO: Keep it safe when using in Prod!!!

    fun hashPassword(password: String): String {
        return bcryptEncoder.encode(password)
    }

    fun verifyPassword(rawPassword: String, hashedPassword: String): Boolean {
        return bcryptEncoder.matches(rawPassword, hashedPassword)
    }

    fun encrypt(value: String): String {
        val key: Key = SecretKeySpec(SECRET_KEY.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encrypted = cipher.doFinal(value.toByteArray())
        return Base64.getEncoder().encodeToString(encrypted)
    }

    fun decrypt(encryptedValue: String): String {
        val key: Key = SecretKeySpec(SECRET_KEY.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, key)
        val decodedValue = Base64.getDecoder().decode(encryptedValue)
        return String(cipher.doFinal(decodedValue))
    }
}
