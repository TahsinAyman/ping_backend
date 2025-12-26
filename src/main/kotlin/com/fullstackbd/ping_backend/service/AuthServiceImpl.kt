package com.fullstackbd.ping_backend.service

import com.fullstackbd.ping_backend.exception.UserNotFoundException
import com.fullstackbd.ping_backend.exception.WrongCredentialException
import com.fullstackbd.ping_backend.model.dto.MailContent
import com.fullstackbd.ping_backend.model.dto.UserDTO
import com.fullstackbd.ping_backend.model.entity.AppService
import com.fullstackbd.ping_backend.model.entity.Role
import com.fullstackbd.ping_backend.model.entity.User
import com.fullstackbd.ping_backend.repository.RoleRepository
import com.fullstackbd.ping_backend.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.get
import org.springframework.cache.set
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.Optional
import java.util.Random
import kotlin.jvm.Throws

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authManager: AuthenticationManager,
    private val tokenService: TokenService<String>,
    private val cacheManager: CacheManager,
    private val mailService: MailService,
    private val roleRepository: RoleRepository
) : AuthService {

    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    override fun register(userDto: UserDTO) {
        val roles: MutableList<Role> = mutableListOf()
        val services: MutableList<AppService> = mutableListOf()
        userDto.password = passwordEncoder.encode(userDto.password)!!
        roleRepository.findFirstByName("USER")?.apply {
            roles.add(this)
            services.addAll(this.services)
        }
        val user = User(
            email = userDto.email,
            password = userDto.password,
            roles = roles,
            services = services
        )
        userRepository.save(user)
    }

    @Throws(WrongCredentialException::class)
    override fun login(userDto: UserDTO): String {
        val auth = authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                userDto.email,
                userDto.password
            )
        )
        if (auth.isAuthenticated) {
            val user = userRepository.findFirstByEmail(userDto.email)!!
            if (!user.verified) {
                throw WrongCredentialException("User is not yet verified")
            }
            val token = tokenService.encode(userDto.email)
            val cache = cacheManager.getCache("active_user")
                ?: throw IllegalStateException("Cache not found")
            val tokens: MutableList<String> = (cache.get(userDto.email, MutableList::class.java)
                ?: mutableListOf<String>()) as MutableList<String>
            tokens.add(token)
            cache.put(userDto.email, tokens)
            return token
        }
        throw WrongCredentialException("Wrong credentials provided")
    }

    override fun logout(token: String): Boolean {
        try {
            val user: UserDetails = SecurityContextHolder.getContext().authentication!!.principal!! as UserDetails
            val email = user.username
            val tokens: MutableList<String> =
                (cacheManager["active_user"]?.get(email)?.get() ?: mutableListOf<String>()) as MutableList<String>
            tokens.remove(token)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    private val fiveDigitNumber: Int
        get() = 10000 + Random().nextInt(90000)


    override fun otpRequest(email: String) {
        userRepository.findFirstByEmail(email) ?: throw UserNotFoundException("No user found with email: $email")
        val otpCache = cacheManager["otp"] ?: throw IllegalStateException("OTP cache not found")
        val otp = fiveDigitNumber
        otpCache.put(email, otp)
        mailService.send(
            MailContent(
                to = email,
                sub = "Your one time password is: $otp",
                body = "Verify your email using OTP: $otp. the code will expire in 10 minutes"
            )
        )
    }

    override fun verifyEmail(email: String, otp: Int) {
        val user = userRepository.findFirstByEmail(email) ?: throw UserNotFoundException("No user found with email: $email")
        val otpCache = cacheManager["otp"] ?: throw IllegalStateException("OTP cache not found")
        val foundOtp: Int = (otpCache.get(email)?.get() ?: 0) as Int
        logger.info("User $email with OTP: $foundOtp")
        if (otp == foundOtp) {
            user.verified = true
            userRepository.save(user)
            return
        }
        throw IllegalArgumentException("OTP is mismatched or never requested")
    }

}
