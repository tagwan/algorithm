package com.github.tagwan.pattern.creational

data class EMail(var recipient: String, var subject: String?, var message: String?)

fun main() {
    val mail = EMail("abc@example.com", "Hello", "Don't know what to write.")
    val copy = mail.copy(recipient = "other@example.com")
}