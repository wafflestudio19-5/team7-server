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

    fun build(links: List<String>, vars: List<String>, type: String): String {
        val context = Context()
        links.mapIndexed { index, link -> context.setVariable("link${index + 1}", link) }
        vars.mapIndexed { index, v -> context.setVariable("var${index + 1}", v) }

        return when (type) {
            "comment-notification" -> templateEngine.process("registerMailTemplate", context)
            else -> ""
        }
    }
}
