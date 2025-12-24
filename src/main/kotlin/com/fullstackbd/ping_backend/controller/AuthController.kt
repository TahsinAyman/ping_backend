package com.fullstackbd.ping_backend.controller

import com.fullstackbd.ping_backend.model.dto.Message
import com.fullstackbd.ping_backend.model.dto.UserDTO
import com.fullstackbd.ping_backend.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val service: AuthService,
) {
    @PostMapping("/register")
    fun register(@RequestBody userDTO: UserDTO): ResponseEntity<Message> {
        try {
            this.service.register(userDTO)
            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                    Message(
                        message = "User registered successfully",
                        status = HttpStatus.CREATED.value()
                    )
                )
        } catch (e: Exception) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                    Message(
                        message = "User already exists",
                        status = HttpStatus.BAD_REQUEST.value()
                    )
                )
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody userDTO: UserDTO): ResponseEntity<Message> {
        try {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                    Message(
                        message = service.login(userDTO),
                        status = HttpStatus.OK.value(),
                    )
                )
        } catch (e: Exception) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                    Message(
                        message = "Invalid Credential given",
                        status = HttpStatus.BAD_REQUEST.value()
                    )
                )
        }
    }
}
