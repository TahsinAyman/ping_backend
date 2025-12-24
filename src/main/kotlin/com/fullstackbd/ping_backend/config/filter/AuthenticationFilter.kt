package com.fullstackbd.ping_backend.config.filter

import com.fullstackbd.ping_backend.model.dto.Message
import com.fullstackbd.ping_backend.model.entity.User
import com.fullstackbd.ping_backend.service.TokenService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import tools.jackson.databind.ObjectMapper
import java.util.Objects

@Component
class AuthenticationFilter(
    private val objMapper: ObjectMapper,
    private val tokenService: TokenService<User>,
    private val userDetailService: UserDetailsService
) : OncePerRequestFilter() {
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
            val user = tokenService.decode(token)
            userDetailService.loadUserByUsername(user.email)
            val authToken: Authentication = UsernamePasswordAuthenticationToken(user, null, user.roles)
            SecurityContextHolder
                .getContext()
                .authentication = authToken
            filterChain.doFilter(request, response)
        } catch (e: Exception) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = "application/json"
            response.writer.write(objMapper.writeValueAsString(
                Message(
                    message = e.message!!,
                    status = HttpStatus.UNAUTHORIZED.value()
                )
            ))
        }
    }
}