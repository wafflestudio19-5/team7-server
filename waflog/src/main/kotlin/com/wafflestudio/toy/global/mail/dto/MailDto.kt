package com.wafflestudio.toy.global.mail.dto

class MailDto {
    data class Email(
        val to : String,
        val subject : String,
        val text : String,
        val withAttachment : Boolean
    )
}