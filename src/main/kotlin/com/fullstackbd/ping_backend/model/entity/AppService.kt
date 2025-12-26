package com.fullstackbd.ping_backend.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fullstackbd.ping_backend.model.dto.HttpMethod
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinColumns
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.hibernate.annotations.Fetch
import org.springframework.security.core.GrantedAuthority

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

    @Column(name = "name", nullable = false, unique = true)
    var name: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "method", nullable = false)
    var method: HttpMethod,
    @Column(name = "url", nullable = false)
    var url: String,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "role_services",
        joinColumns = [JoinColumn("service_id")],
        inverseJoinColumns = [JoinColumn("role_id")]
    )
    @JsonIgnore
    var roles: MutableList<Role> = mutableListOf(),

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_services",
        joinColumns = [JoinColumn("service_id")],
        inverseJoinColumns = [JoinColumn("user_id")]
    )
    @JsonIgnore
    var users: MutableList<User> = mutableListOf()
) {
    override fun toString(): String {
        return "AppService(url='$url', method=$method, name='$name', id=$id)"
    }
}
