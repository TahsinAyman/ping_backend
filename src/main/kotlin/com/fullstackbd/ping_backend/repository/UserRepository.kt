package com.fullstackbd.ping_backend.repository

import com.fullstackbd.ping_backend.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findFirstByEmail(email: String): User?
}
