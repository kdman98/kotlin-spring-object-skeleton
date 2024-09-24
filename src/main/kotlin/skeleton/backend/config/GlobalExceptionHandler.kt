package skeleton.backend.config

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(AuthorizationFailException::class)
    fun handleUserNotFoundException(ex: AuthorizationFailException): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(
            resultCode = HttpStatus.UNAUTHORIZED.value(),
            resultMsg = ex.message ?: "user not found",
        )
        println(ex.printStackTrace())
        return ResponseEntity(response, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(ex: BadRequestException): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(
            resultCode = HttpStatus.BAD_REQUEST.value(),
            resultMsg = ex.message ?: "bad request",
        )
        println(ex.printStackTrace())
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(GeneralApiException::class)
    fun handleGeneralApiException(ex: GeneralApiException): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(
            resultCode = ex.errorCode,
            resultMsg = ex.message ?: "error caused",
        )
        println(ex.printStackTrace())
        return ResponseEntity(response, HttpStatus.valueOf(ex.errorCode))
    }
}

data class ErrorResponse(
    val resultCode: Int,
    val resultMsg: String,
)

class AuthorizationFailException(message: String) : RuntimeException(message)
class BadRequestException(message: String) : RuntimeException(message)
class GeneralApiException(message: String, val errorCode: Int) : RuntimeException(message)
