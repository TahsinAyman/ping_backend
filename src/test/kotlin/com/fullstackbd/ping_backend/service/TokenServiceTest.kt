package com.fullstackbd.ping_backend.service

import com.fullstackbd.ping_backend.model.entity.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class TokenServiceTest {

    @Autowired
    private lateinit var service: TokenService<User>

    @Test
    fun encode() {
        val user = User(
            email = "mail",
            password = "for",
            roles = mutableListOf(),
            services = mutableListOf()
        )
        println(service.encode(user))
    }

    @Test
    fun decode() {
        val token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJpZFwiOm51bGwsXCJlbWFpbFwiOlwibWFpbFwiLFwicGFzc3dvcmRcIjpcImZvclwiLFwicm9sZXNcIjpbXSxcInNlcnZpY2VzXCI6W10sXCJhY2NvdW50Tm9uRXhwaXJlZFwiOnRydWUsXCJhY2NvdW50Tm9uTG9ja2VkXCI6dHJ1ZSxcImF1dGhvcml0aWVzXCI6W10sXCJjcmVkZW50aWFsc05vbkV4cGlyZWRcIjp0cnVlLFwiZW5hYmxlZFwiOnRydWUsXCJ1c2VybmFtZVwiOlwibWFpbFwifSIsImV4cCI6MTc2NjYxNjc0OCwiaWF0IjoxNzY2NjEzMTQ4fQ.Vu5iPx91cxUvkIcL1p-c2mdm1Db14LPCMwnI-kwZDmfGLJsaxqBrctSVMD65Qrs1UIMaSB0RX3XGiTZ6Zd3pKA"
        println(service.decode(token).email)
    }

}