package com.fullstackbd.ping_backend.init

import com.fullstackbd.ping_backend.model.dto.HttpMethod
import com.fullstackbd.ping_backend.model.entity.AppService
import com.fullstackbd.ping_backend.model.entity.Role
import com.fullstackbd.ping_backend.model.entity.User
import com.fullstackbd.ping_backend.repository.RoleRepository
import com.fullstackbd.ping_backend.repository.ServiceRepository
import com.fullstackbd.ping_backend.repository.UserRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootTest
class DefaultSetup{

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var roleRepository: RoleRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var serviceRepository: ServiceRepository

    @Test
    fun init() {
        val userRole = roleRepository.findFirstByName("USER")!!
        userRepository.save(
            User(
                email = "test@example.com",
                password = passwordEncoder.encode("123")!!,
                roles = mutableListOf(userRole),
                services = userRole.services
            )
        )
    }

    @Test
    fun servicesAndRolesInit() {
        var services = listOf(
            AppService(
                name = "PUBLIC_API",
                method = HttpMethod.GET,
                url = "/public",
            )
        )
        services = serviceRepository.saveAll<AppService>(services)
        var roles = listOf(
            Role(
                name = "USER",
                services = services
            )
        )
        roles = roleRepository.saveAll(roles.toMutableList())
    }

    @Test
    fun test() {
//        val user = userRepository.findFirstByEmail("test@example.com")!!
//        user.roles = mutableListOf(roleRepository.findFirstByName("USER")!!)
//        user.services = roleRepository.findFirstByName("USER")!!.services
//        userRepository.save(user)

//        roleRepository.findFirstByName("USER")!!.services.add(
//            serviceRepository.findFirstByName("LOGOUT_API")!!
//        )
        roleRepository.findAll().forEach(System.out::println)
        userRepository.delete(userRepository.findFirstByEmail("mail4tahsin1@gmail.com")!!)
    }
}