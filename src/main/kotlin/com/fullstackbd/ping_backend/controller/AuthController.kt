package com.fullstackbd.ping_backend.controller

import com.fullstackbd.ping_backend.model.dto.Message
import com.fullstackbd.ping_backend.model.dto.OtpRequestDTO
import com.fullstackbd.ping_backend.model.dto.UserDTO
import com.fullstackbd.ping_backend.model.dto.VerifyEmailDTO
import com.fullstackbd.ping_backend.model.entity.User
import com.fullstackbd.ping_backend.service.AuthService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val service: AuthService
) {
    private val log = LoggerFactory.getLogger(this.javaClass.name)

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
            log.error(e.message)
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                    Message(
                        message = e.message!!,
                        status = HttpStatus.BAD_REQUEST.value()
                    )
                )
        }
    }

    @PostMapping("/send-otp")
    fun sendOtp(@RequestBody otpRequestDTO: OtpRequestDTO): ResponseEntity<Message> {
        try {
            service.otpRequest(otpRequestDTO.email)
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                    Message(
                        message = "Check your inbox / spam",
                        status = HttpStatus.OK.value()
                    )
                )
        } catch (e: Exception) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                    Message(
                        message = e.message!!,
                        status = HttpStatus.BAD_REQUEST.value()
                    )
                )
        }
    }

    @DeleteMapping("/logout")
    fun logout(@RequestHeader("Authorization") token: String): ResponseEntity<Message> {
        if (service.logout(token)) {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                    Message(
                        message = "Session Logged out",
                        status = HttpStatus.OK.value()
                    )
                )
        }
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                Message(
                    message = "Something went wrong",
                    status = HttpStatus.BAD_REQUEST.value()
                )
            )
    }

    @PostMapping("/verify")
    fun verify(@RequestBody verifyDTO: VerifyEmailDTO): ResponseEntity<Message> {
        try {
            service.verifyEmail(verifyDTO.email, verifyDTO.otp)
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                    Message(
                        message = "Email verified",
                        status = HttpStatus.OK.value()
                    )
                )
        } catch (e: Exception) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                    Message(
                        message = e.message!!,
                        status = HttpStatus.BAD_REQUEST.value()
                    )
                )
        }
    }


}
