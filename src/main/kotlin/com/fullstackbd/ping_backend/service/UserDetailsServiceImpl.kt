package com.fullstackbd.ping_backend.service

import com.fullstackbd.ping_backend.exception.UserNotFoundException
import com.fullstackbd.ping_backend.repository.UserRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

@Service
class UserDetailsServiceImpl(
    private val userRepository: UserRepository
) : UserDetailsService {

    @Throws(UserNotFoundException::class)
    @Cacheable("users")
    override fun loadUserByUsername(username: String): UserDetails {
        val user = this.userRepository.findFirstByEmail(username)
            ?: throw UserNotFoundException("User not found by email: $username")
        return user
    }
}
