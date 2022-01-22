package com.wafflestudio.waflog.global.mail.service

import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@Service
class MailContentBuilder(
    private val templateEngine: TemplateEngine
) {
    fun build(message: String, type: String): String {
        val context = Context()
        context.setVariable("link", message)
        return when (type) {
            "register" -> templateEngine.process("registerMailTemplate", context)
            "email-login" -> templateEngine.process("loginMailTemplate", context)
            else -> ""
        }
    }
}
