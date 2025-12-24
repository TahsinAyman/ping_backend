package com.fullstackbd.ping_backend.service

import com.fullstackbd.ping_backend.exception.TokenDecodeException

interface TokenService<T> {
    fun encode(obj: T): String

    @Throws(TokenDecodeException::class)
    fun decode(token: String): T
}