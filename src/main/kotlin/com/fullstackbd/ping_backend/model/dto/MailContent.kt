package com.fullstackbd.ping_backend.model.dto

data class MailContent(
    var to: String,
    var sub: String,
    var body: String
)
