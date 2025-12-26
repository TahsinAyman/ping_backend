package com.fullstackbd.ping_backend.service

import com.fullstackbd.ping_backend.model.dto.MailContent

interface MailService {
    fun send(content: MailContent, html: Boolean = false): Boolean
}