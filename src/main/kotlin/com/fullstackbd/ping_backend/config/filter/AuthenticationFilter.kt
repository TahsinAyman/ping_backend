package com.fullstackbd.ping_backend.config.filter

import com.fullstackbd.ping_backend.exception.InvalidTokenException
import com.fullstackbd.ping_backend.service.TokenService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.cache.CacheManager
import org.springframework.cache.get
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

@Component
class AuthenticationFilter(
    private val tokenService: TokenService<String>,
    private val userDetailService: UserDetailsService,
    private val cacheManager: CacheManager
) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(AuthenticationFilter::class.java.name)

    private fun authenticate(user: UserDetails) {
        val authToken: Authentication = UsernamePasswordAuthenticationToken(user, null, user.authorities)
        SecurityContextHolder
            .getContext()
            .authentication = authToken
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val token = request.getHeader("AUTHORIZATION")
            if (Objects.isNull(token) || token.equals("")) {
                throw RuntimeException("No AUTHORIZATION header was found")
            }
            val email = tokenService.decode(token)
            val tokens: List<String> = (cacheManager["active_user"]?.get(email)?.get() ?: emptyList<String>()) as List<String>
            if (!tokens.contains(token)) {
                throw InvalidTokenException("$token is invalid")
            }
            val user = userDetailService.loadUserByUsername(email)
            authenticate(user)
        } catch (e: Exception) {
            log.error("${request.method} ${request.requestURL}: ${e.message}")
        }
        filterChain.doFilter(request, response)
    }
}