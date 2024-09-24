package skeleton.backend.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import skeleton.backend.util.JwtUtil

@Component
class JwtAuthenticationFilter() : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            val authorizationHeader = request.getHeader("Authorization")

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                val token = authorizationHeader.substring(7)
                if (JwtUtil.validateToken(token)) {
                    val username = JwtUtil.extractUsername(token)
                    val authentication = UsernamePasswordAuthenticationToken(username, null, emptyList())
                    SecurityContextHolder.getContext().authentication = authentication
                } else {
                    throw AuthorizationFailException("사용할 수 없는 토큰입니다.")
                }
            }
            filterChain.doFilter(request, response)
        } catch (e: AuthorizationFailException) {
            handleException(response, e, HttpServletResponse.SC_UNAUTHORIZED)
        }
    }

    private fun handleException(response: HttpServletResponse, ex: Exception, status: Int) {
        println(ex.printStackTrace())
        response.status = status
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.write(
            """{
            "resultCode": $status,
            "resultMsg": "${ex.message}"
        }""".trimIndent()
        )
    }
}
