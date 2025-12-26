package com.fullstackbd.ping_backend.model.dto

data class VerifyEmailDTO(
    var email: String,
    var otp: Int
)
