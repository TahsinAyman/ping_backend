package com.fullstackbd.ping_backend.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import org.springframework.security.core.GrantedAuthority

@Entity
@Table(name = "roles")
data class Role (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(name = "name", unique = true, nullable = false)
    var name: String,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "role_services",
        joinColumns = [JoinColumn("role_id")],
        inverseJoinColumns = [JoinColumn("service_id")]
    )
    var services: MutableList<AppService> = mutableListOf(),
    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn("role_id")],
        inverseJoinColumns = [JoinColumn("user_id")]
    )
    var users: MutableList<User> = mutableListOf()
) {
    override fun toString(): String {
        return "Role(services=$services, name='$name', id=$id)"
    }
}
