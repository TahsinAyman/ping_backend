package com.fullstackbd.ping_backend.service

import com.fullstackbd.ping_backend.exception.WrongCredentialException
import com.fullstackbd.ping_backend.model.dto.UserDTO
import com.fullstackbd.ping_backend.model.entity.User
import com.fullstackbd.ping_backend.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authManager: AuthenticationManager,
    private val tokenService: TokenService<User>
) : AuthService {

    override fun register(userDto: UserDTO) {
        userDto.password = passwordEncoder.encode(userDto.password)!!
        val user = User(
            email = userDto.email,
            password = userDto.password,
            roles = mutableListOf(),
            services = mutableListOf()
        )
        userRepository.save(user)
    }

    override fun login(userDto: UserDTO): String {
        val auth = authManager.authenticate(UsernamePasswordAuthenticationToken(
            userDto.email,
            userDto.password
        ))
        if (auth.isAuthenticated) {
            val user = userRepository.findFirstByEmail(userDto.email)
            return tokenService.encode(user!!)
        }
        throw WrongCredentialException("Wrong credentials provided")
    }

}
