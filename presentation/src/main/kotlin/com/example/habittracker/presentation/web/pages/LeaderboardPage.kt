package com.example.habittracker.presentation.web.pages

import com.example.habittracker.application.DashboardView
import com.example.habittracker.presentation.web.i18n.UiMessages
import com.example.habittracker.presentation.web.layout.renderPage
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.p
import kotlinx.html.section
import kotlinx.html.span

fun leaderboardPage(
    dashboard: DashboardView,
    messages: UiMessages,
    isLoggedIn: Boolean,
    csrf: CsrfView?,
): String = renderPage(messages.text("page.leaderboard.title"), "Leaderboard", messages, isLoggedIn, csrf) {
    section("page-heading") {
        span("eyebrow") { +messages.text("page.leaderboard.eyebrow") }
        h1 { +messages.text("page.leaderboard.heading") }
        p { +messages.text("page.leaderboard.subtitle") }
    }

    section("leaderboard") {
        dashboard.leaderboard.forEach { entry ->
            div("leaderboard-row") {
                span("rank") { +"#${entry.rank} ✨" }
                div("avatar") { +entry.initials }
                div("leader-name") {
                    span { +entry.name }
                    span("muted") { +messages.text("metric.dayStreak", entry.streakDays) }
                }
                div("leader-score") {
                    span { +messages.text("metric.points", entry.score) }
                    span("muted") { +messages.text("metric.doneThisWeek", entry.completedThisWeek) }
                }
            }
        }
    }
}
