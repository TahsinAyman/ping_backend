package com.fullstackbd.ping_backend.model.entity

import com.fullstackbd.ping_backend.model.dto.HttpMethod
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinColumns
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "services",
    uniqueConstraints = [
        UniqueConstraint(
            columnNames = ["method", "url"]
        )
    ]
)
data class AppService(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Enumerated(EnumType.STRING)
    @Column(name = "method", nullable = false)
    var method: HttpMethod,
    @Column(name = "url", nullable = false)
    var url: String,

    @ManyToMany
    @JoinTable(
        name = "role_services",
        joinColumns = [JoinColumn(name = "role_id")],
        inverseJoinColumns = [JoinColumn(name = "service_id")]
    )
    var roles: MutableList<Role>,

    @ManyToMany
    @JoinTable(
        name = "user_services",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "service_id")]
    )
    var users: MutableList<User>
)
