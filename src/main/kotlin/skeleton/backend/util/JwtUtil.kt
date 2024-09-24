package skeleton.backend.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.time.Duration
import java.util.*

object JwtUtil {
    private const val SECRET_KEY = "JzYW1wbGUxQGdtYWlsLmNvbSIsImlhdCI6MTcyNjMwOTIxOCwiZXhwIjoxNzI2MzE2NDE4fQzz/"
    // TODO: Keep it safe when using in Prod!!!

    private val secretKeyBytes = SECRET_KEY.toByteArray()
    private val signingKey = Keys.hmacShaKeyFor(secretKeyBytes)

    fun generateToken(username: String): String {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + Duration.ofMinutes(30).toMillis()))
            .signWith(signingKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            val claims: Claims = extractAllClaims(token)
            !claims.expiration.before(Date())
        } catch (e: Exception) {
            false
        }
    }

    fun extractUsername(token: String): String {
        return extractAllClaims(token).subject
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser()
            .setSigningKey(signingKey)
            .build()
            .parseClaimsJws(token)
            .body
    }
}
