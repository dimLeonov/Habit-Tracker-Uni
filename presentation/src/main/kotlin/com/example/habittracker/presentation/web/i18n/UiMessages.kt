package com.example.habittracker.presentation.web.i18n

import org.springframework.context.MessageSource
import java.util.Locale

class UiMessages(private val messageSource: MessageSource, private val locale: Locale) {
    fun text(code: String, vararg args: Any): String = messageSource.getMessage(code, args, locale)
}
