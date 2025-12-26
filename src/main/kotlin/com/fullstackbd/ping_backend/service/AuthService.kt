package com.fullstackbd.ping_backend.service

import com.fullstackbd.ping_backend.model.dto.UserDTO

interface AuthService {
    fun register(userDto: UserDTO)
    fun login(userDto: UserDTO): String
    fun logout(token: String): Boolean
    fun otpRequest(email: String)
    fun verifyEmail(email: String, otp: Int)
}