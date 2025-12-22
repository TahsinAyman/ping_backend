package com.fullstackbd.ping_backend.controller

import com.fullstackbd.ping_backend.model.dto.Message
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin(origins = ["*"])
class HomeController {
    @GetMapping("/")
    fun home(): ResponseEntity<Message> = ResponseEntity.status(HttpStatus.OK).body(
        Message(
            message = "Hello world",
            status = HttpStatus.OK.value()
        )
    )

    @GetMapping("/public")
    fun public(): ResponseEntity<Message> = ResponseEntity
        .status(HttpStatus.OK)
        .body(
            Message(
                message = "Public API",
                status = HttpStatus.OK.value()
            )
        )
}
