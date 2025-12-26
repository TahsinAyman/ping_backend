package com.fullstackbd.ping_backend.service

import com.fullstackbd.ping_backend.model.dto.MailContent
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MailServiceTest {

    @Autowired
    private lateinit var service: MailService

    @Test
    fun test() {
        service.send(
            MailContent(
                to = "mail4tahsin@gmail.com",
                sub = "Just a quick greeting",
                body = "<h1>Looking for HTML</h1>"
            ),
            true
        )
    }
}