package com.fullstackbd.ping_backend.service

import com.fullstackbd.ping_backend.model.entity.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper
import java.util.*

@Component
class JwtService(
    private val objMapper: ObjectMapper
): TokenService<User> {

    @Value($$"${jwt.secret}")
    private lateinit var secret: String

    @Value($$"${jwt.expiration}")
    private var expiration: Int = 3600

    override fun encode(obj: User): String {
        val now = Date()
        val expiry = Date(now.time + expiration * 1000)
        val userJson = objMapper.writeValueAsString(obj)

        return Jwts
            .builder()
            .setSubject(userJson)
            .setExpiration(expiry)
            .setIssuedAt(now)
            .signWith(Keys.hmacShaKeyFor(secret.toByteArray()))
            .compact()
    }

    override fun decode(token: String): User {
        val sub: String = Jwts
            .parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secret.toByteArray()))
            .build()
            .parseClaimsJws(token)
            .body
            .subject
        return objMapper.readValue(sub, User::class.java)
    }

}