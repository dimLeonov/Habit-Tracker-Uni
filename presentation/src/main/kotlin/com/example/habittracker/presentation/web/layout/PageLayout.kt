package com.example.habittracker.presentation.web.layout

import com.example.habittracker.presentation.web.i18n.UiMessages
import com.example.habittracker.presentation.web.pages.CsrfView
import com.example.habittracker.presentation.web.pages.csrfInput
import kotlinx.html.ButtonType
import kotlinx.html.FlowContent
import kotlinx.html.FormMethod
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.head
import kotlinx.html.html
import kotlinx.html.lang
import kotlinx.html.link
import kotlinx.html.main
import kotlinx.html.meta
import kotlinx.html.nav
import kotlinx.html.stream.createHTML
import kotlinx.html.title

fun renderPage(
    pageTitle: String,
    active: String,
    messages: UiMessages,
    isLoggedIn: Boolean,
    csrf: CsrfView? = null,
    content: FlowContent.() -> Unit,
): String = "<!doctype html>\n" +
    createHTML().html {
        lang = "en"
        head {
            meta(charset = "utf-8")
            meta(name = "viewport", content = "width=device-width, initial-scale=1")
            title(pageTitle)
            link(rel = "stylesheet", href = "/styles/app.css")
        }
        body {
            div("app-shell") {
                nav("topbar") {
                    a(classes = "brand", href = "/") {
                        +messages.text("app.brand")
                    }
                    div("nav-links") {
                        navigationLink("/", messages.text("nav.home"), "Home", active)
                        navigationLink("/workspace", messages.text("nav.workspace"), "Workspace", active)
                        navigationLink("/habits", messages.text("nav.habitPlace"), "Habit Place", active)
                        navigationLink("/calendar", messages.text("nav.calendar"), "Calendar", active)
                        navigationLink("/leaderboard", messages.text("nav.leaderboard"), "Leaderboard", active)
                    }
                    div("auth-links") {
                        if (isLoggedIn) {
                            form(action = "/logout", method = FormMethod.post, classes = "inline-form") {
                                csrfInput(csrf)
                                button(classes = "logout-link", type = ButtonType.submit) {
                                    +messages.text("nav.logout")
                                }
                            }
                        } else {
                            a(classes = "ghost-link", href = "/login") { +messages.text("nav.login") }
                            a(classes = "primary-link", href = "/register") { +messages.text("nav.register") }
                        }
                    }
                }
                main {
                    content()
                }
            }
        }
    }

private fun FlowContent.navigationLink(
    href: String,
    label: String,
    activeKey: String,
    active: String,
) {
    a(classes = if (active == activeKey) "nav-link active" else "nav-link", href = href) {
        +label
    }
}
