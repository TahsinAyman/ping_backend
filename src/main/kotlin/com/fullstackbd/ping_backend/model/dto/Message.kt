package com.fullstackbd.ping_backend.model.dto


data class Message(
    var message: String,
    var status: Int,
    var version: String = "1.0.0"
)