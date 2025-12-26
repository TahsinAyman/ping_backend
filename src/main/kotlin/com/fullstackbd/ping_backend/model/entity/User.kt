package com.fullstackbd.ping_backend.model.entity

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
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(name = "email", unique = true, nullable = false)
    var email: String,
    @Column(name = "password", nullable = false)
    private var password: String,
    @Column(name = "verified", nullable = false, columnDefinition = "boolean default false")
    var verified: Boolean = false,
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn("user_id")],
        inverseJoinColumns = [JoinColumn("role_id")]
    )
    var roles: MutableList<Role> = mutableListOf(),
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_services",
        joinColumns = [JoinColumn("user_id")],
        inverseJoinColumns = [JoinColumn("service_id")]
    )
    var services: MutableList<AppService> = mutableListOf()

) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> = this.services
        .map { SimpleGrantedAuthority(it.name) }

    override fun getPassword(): String? = this.password
    override fun getUsername(): String = this.email
    fun setPassword(password: String) {
        this.password = password
    }
}
