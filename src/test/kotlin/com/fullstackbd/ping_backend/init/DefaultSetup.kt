package com.fullstackbd.ping_backend.init

import com.fullstackbd.ping_backend.model.entity.Role
import com.fullstackbd.ping_backend.model.entity.User
import com.fullstackbd.ping_backend.repository.RoleRepository
import com.fullstackbd.ping_backend.repository.ServiceRepository
import com.fullstackbd.ping_backend.repository.UserRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class DefaultSetup {

    @Autowired
    private lateinit var userRepository: UserRepository
    
    @Autowired
    private lateinit var roleRepository: RoleRepository
    
    @Autowired
    private lateinit var serviceRepository: ServiceRepository
    
    @Test
    fun init() {
        userRepository.save(
            User(
                email = "test@example.com",
                password = "123",
                roles = mutableListOf(),
                services = mutableListOf()
            )
        )
    }
}