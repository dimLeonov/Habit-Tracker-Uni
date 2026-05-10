package com.example.habittracker.presentation.web.pages

import com.example.habittracker.presentation.web.i18n.UiMessages
import com.example.habittracker.presentation.web.layout.renderPage
import kotlinx.html.ButtonType
import kotlinx.html.FORM
import kotlinx.html.FlowContent
import kotlinx.html.FormMethod
import kotlinx.html.InputType
import kotlinx.html.a
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h1
import kotlinx.html.input
import kotlinx.html.label
import kotlinx.html.p
import kotlinx.html.section
import kotlinx.html.strong

private const val LOGIN_LABEL = "Login"

fun loginPage(
    csrf: CsrfView?,
    hasError: Boolean,
    messages: UiMessages,
): String = renderPage(messages.text("auth.login.title"), LOGIN_LABEL, messages, false) {
    authShell(
        title = messages.text("auth.login.heading"),
        subtitle = messages.text("auth.login.subtitle"),
        switchText = messages.text("auth.login.switchText"),
        switchHref = "/register",
        switchLabel = messages.text("nav.register"),
        action = "/login",
    ) {
        csrfInput(csrf)
        label {
            +messages.text("form.username")
            input(type = InputType.text, name = "username") {
                placeholder = "Your username"
            }
        }
        label {
            +messages.text("form.password")
            input(type = InputType.password, name = "password") {
                placeholder = "Your password"
            }
        }
        if (hasError) {
            p("form-error") { +messages.text("auth.login.error") }
        }
        button(classes = "button primary full", type = ButtonType.submit) {
            +messages.text("page.home.login")
        }
    }
}

fun registerPage(
    csrf: CsrfView?,
    error: String?,
    messages: UiMessages,
): String = renderPage(messages.text("auth.register.title"), "Register", messages, false) {
    authShell(
        title = messages.text("auth.register.heading"),
        subtitle = messages.text("auth.register.subtitle"),
        switchText = messages.text("auth.register.switchText"),
        switchHref = "/login",
        switchLabel = messages.text("nav.login"),
        action = "/register",
    ) {
        csrfInput(csrf)
        label {
            +messages.text("form.username")
            input(type = InputType.text, name = "username") {
                placeholder = "yourname"
            }
        }
        label {
            +messages.text("form.password")
            input(type = InputType.password, name = "password") {
                placeholder = "Create password"
            }
        }
        if (error != null) {
            p("form-error") { +error }
        }
        button(classes = "button primary full", type = ButtonType.submit) {
            +"🌟 Create account"
        }
    }
}

private fun FlowContent.authShell(
    title: String,
    subtitle: String,
    switchText: String,
    switchHref: String,
    switchLabel: String,
    action: String,
    fields: FORM.() -> Unit,
) {
    section("auth-page") {
        div("auth-copy") {
            h1 { +title }
            p { +subtitle }
            p("auth-switch") {
                +switchText
                +" "
                a(href = switchHref) { strong { +switchLabel } }
            }
        }
        form(action = action, method = FormMethod.post, classes = "auth-card") {
            fields()
        }
    }
}
