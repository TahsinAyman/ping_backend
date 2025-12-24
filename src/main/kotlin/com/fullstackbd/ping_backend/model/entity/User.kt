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
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
data class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(name = "email", unique = true, nullable = false)
    var email: String,
    @Column(name = "password", nullable = false)
    private var password: String,

    @ManyToMany
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "role_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    var roles: MutableList<Role>,

    @ManyToMany
    @JoinTable(
        name = "user_services",
        joinColumns = [JoinColumn(name = "service_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    var services : MutableList<AppService>

) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> = this.roles
    override fun getPassword(): String? = this.password
    override fun getUsername(): String = this.email
    fun setPassword(password: String) {
        this.password = password
    }
}
