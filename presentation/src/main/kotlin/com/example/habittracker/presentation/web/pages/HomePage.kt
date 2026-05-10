package com.example.habittracker.presentation.web.pages

import com.example.habittracker.application.DashboardView
import com.example.habittracker.application.HabitStatusView
import com.example.habittracker.presentation.web.i18n.UiMessages
import com.example.habittracker.presentation.web.layout.renderPage
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.p
import kotlinx.html.section
import kotlinx.html.span

private const val PREVIEW_HABIT_LIMIT = 3
fun homePage(
    dashboard: DashboardView,
    messages: UiMessages,
    isLoggedIn: Boolean,
    csrf: CsrfView?,
): String = renderPage(messages.text("page.home.title"), "Home", messages, isLoggedIn, csrf) {
    section("hero home-hero") {
        div("hero-copy") {
            span("eyebrow") { +messages.text("page.home.eyebrow") }
            h1 { +messages.text("page.home.heading") }
            p {
                +messages.text("page.home.subtitle")
            }
            div("hero-actions") {
                a(classes = "button primary", href = "/register") { +messages.text("page.home.createAccount") }
                a(classes = "button secondary", href = "/login") { +messages.text("page.home.login") }
            }
        }
        div("hero-panel") {
            h2 { +messages.text("page.home.today") }
            dashboard.habits.take(PREVIEW_HABIT_LIMIT).forEach { habit ->
                div("mini-habit") {
                    span { +habit.name }
                    span("status-pill ${habit.status.name.lowercase()}") {
                        +if (habit.status == HabitStatusView.DONE) {
                            messages.text("action.done")
                        } else {
                            messages.text("action.open")
                        }
                    }
                }
            }
        }
    }

    section("stats-grid") {
        dashboard.stats.forEach { stat ->
            div("stat-card") {
                span("stat-label") { +stat.label }
                span("stat-value") { +stat.value }
                span("stat-detail") { +stat.detail }
            }
        }
    }
}
