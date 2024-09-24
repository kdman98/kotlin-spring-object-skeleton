package skeleton.backend.controller

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import skeleton.backend.api.LoginRequest
import skeleton.backend.api.LoginResponse
import skeleton.backend.api.SignupRequest
import skeleton.backend.api.SimpleResponse
import skeleton.backend.enums.UserTypeEnum
import skeleton.backend.exposed.domain.User
import skeleton.backend.service.UserService


@RestController
@RequestMapping("/user")
class UserController {
    private val userService by lazy { UserService }

    @PostMapping("/signup")
    fun signup(@RequestBody signupRequest: SignupRequest): ResponseEntity<SimpleResponse> {
        val user = User(
            userId = signupRequest.userId,
            password = signupRequest.password,
            name = signupRequest.name,
            userType = UserTypeEnum.fromIdType(signupRequest.idType),
            idValue = signupRequest.idValue,
        )
        userService.signup(user)
        val response = SimpleResponse(resultCode = 200, resultMsg = "OK")
        return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(response)
    }

    @PostMapping("/login")
    fun login(
        @RequestBody loginRequest: LoginRequest,
    ): ResponseEntity<LoginResponse> {
        val token = UserService.processLoginAndReturnToken(loginRequest.userId, loginRequest.password)
        val response = LoginResponse(resultCode = 200, resultMsg = "OK", token = token)
        return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(response)
    }
}
