package com.fullstackbd.ping_backend.service

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.userdetails.UserDetailsService

@SpringBootTest
class UserDetailsServiceTest {

    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    @Test
    fun test() {
        println(
            userDetailsService.loadUserByUsername("test@example.com")
                .password
        )
    }

}