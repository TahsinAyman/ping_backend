package com.fullstackbd.ping_backend.service

import com.fullstackbd.ping_backend.model.dto.MailContent
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class MailServiceImpl(
    private val javaMailSender: JavaMailSender
) : MailService {

    override fun send(content: MailContent, html: Boolean): Boolean {
        val mimeMessage = javaMailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage, true, "UTF-8")
        helper.apply {
            setTo(content.to)
            setSubject(content.sub)
            setText(content.body, html)
        }
        javaMailSender.send(mimeMessage)
        return false
    }

}