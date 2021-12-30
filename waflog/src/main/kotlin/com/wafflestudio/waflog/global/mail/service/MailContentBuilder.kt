package com.wafflestudio.waflog.global.mail.service

import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@Service
class MailContentBuilder(
    private val templateEngine: TemplateEngine
) {
    fun build(message: String): String {
        val context = Context()
        context.setVariable("link", message)
        return templateEngine.process("mailTemplate", context)
    }
}
