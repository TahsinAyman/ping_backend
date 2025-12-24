package com.fullstackbd.ping_backend.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
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

    @ManyToMany
    @JoinTable(
        name = "role_services",
        joinColumns = [JoinColumn(name = "service_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var services: MutableList<AppService>,
    @ManyToMany
    @JoinTable(
        name = "role_services",
        joinColumns = [JoinColumn(name = "role_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    var users: MutableList<User>
) : GrantedAuthority {
    override fun getAuthority(): String? = this.name
}
