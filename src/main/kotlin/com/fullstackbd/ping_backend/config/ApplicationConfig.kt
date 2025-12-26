package com.fullstackbd.ping_backend.config

import com.fullstackbd.ping_backend.model.entity.User
import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.Duration

@Configuration
class ApplicationConfig {

    @Value($$"${jwt.expiration}")
    private var expiry: Long = 0

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(
        config: AuthenticationConfiguration
    ): AuthenticationManager = config.authenticationManager

    @Bean
    fun cacheManager(): CacheManager {
        val tokenCache = CaffeineCache(
            "active_user", Caffeine
                .newBuilder()
                .expireAfterWrite(Duration.ofSeconds(expiry))
                .build()
        )
        val userCache = CaffeineCache(
            "users", Caffeine
                .newBuilder()
                .expireAfterWrite(Duration.ofDays(30))
                .build()
        )
        val otpCache = CaffeineCache(
            "otp", Caffeine
                .newBuilder()
                .expireAfterWrite(Duration.ofMinutes(10))
                .build()
        )
        return SimpleCacheManager().apply {
            setCaches(listOf(tokenCache, userCache, otpCache))
        }
    }
}