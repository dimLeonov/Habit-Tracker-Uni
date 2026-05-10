@file:Suppress("MatchingDeclarationName")

package com.example.habittracker.presentation.web.pages

import kotlinx.html.FlowContent
import kotlinx.html.hiddenInput

data class CsrfView(
    val parameterName: String,
    val token: String,
)

fun FlowContent.csrfInput(csrf: CsrfView?) {
    if (csrf != null) {
        hiddenInput(name = csrf.parameterName) {
            value = csrf.token
        }
    }
}
