package com.fullstackbd.ping_backend.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationEventPublisher
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
@Configuration
class SecurityConfig {
    @Bean
    @ConditionalOnMissingBean(UserDetailsService::class)
    fun inMemoryUserDetailsManager(): InMemoryUserDetailsManager {
        val password: String = "{noop}123"
        return InMemoryUserDetailsManager(
            User.withUsername("user")
                .password(password)
                .roles("USER")
                .build()
        )
    }
    @Bean
    @ConditionalOnMissingBean(AuthenticationEventPublisher::class)
    fun defaultAuthenticationEventPublisher(delegate: ApplicationEventPublisher): DefaultAuthenticationEventPublisher {
        return DefaultAuthenticationEventPublisher(delegate)
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/public")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            }


        return http.build()
    }

}