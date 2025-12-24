package com.fullstackbd.ping_backend.repository

import com.fullstackbd.ping_backend.model.entity.AppService
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ServiceRepository : JpaRepository<AppService, Long> {
}
