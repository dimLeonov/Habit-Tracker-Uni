package com.example.habittracker.presentation.web.pages

import com.example.habittracker.application.CalendarDayView
import com.example.habittracker.application.CalendarView
import com.example.habittracker.presentation.web.i18n.UiMessages
import com.example.habittracker.presentation.web.layout.renderPage
import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.p
import kotlinx.html.section
import kotlinx.html.span
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

private val MONTH_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
private val WEEKDAY_LABELS = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

fun calendarPage(
    calendar: CalendarView,
    messages: UiMessages,
    locale: Locale,
    csrf: CsrfView?,
): String = renderPage(messages.text("page.calendar.title"), "Calendar", messages, true, csrf) {
    calendarHeading(calendar, messages, locale)
    calendarGrid(calendar, messages)
}

private fun FlowContent.calendarHeading(
    calendar: CalendarView,
    messages: UiMessages,
    locale: Locale,
) {
    section("page-heading calendar-heading") {
        span("eyebrow") { +messages.text("page.calendar.eyebrow") }
        h1 { +messages.text("page.calendar.heading", calendar.userName) }
        p { +messages.text("page.calendar.subtitle") }
        div("calendar-toolbar") {
            a(classes = "button secondary", href = "/calendar?month=${calendar.previousMonth}") {
                +messages.text("page.calendar.previous")
            }
            span("calendar-month") {
                +YearMonth.parse(calendar.month).format(MONTH_FORMATTER.withLocale(locale))
            }
            a(classes = "button secondary", href = "/calendar?month=${calendar.nextMonth}") {
                +messages.text("page.calendar.next")
            }
        }
        div("calendar-score") {
            span("stat-label") { +messages.text("page.calendar.monthScore") }
            span("stat-value") { +"${calendar.completedDays}/${calendar.totalHabitDays}" }
        }
    }
}

private fun FlowContent.calendarGrid(calendar: CalendarView, messages: UiMessages) {
    section("calendar-card") {
        div("calendar-weekdays") {
            WEEKDAY_LABELS.forEach { weekday -> span { +weekday } }
        }
        calendar.weeks.forEach { week ->
            div("calendar-week") {
                week.days.forEach { day -> calendarDay(day, messages) }
            }
        }
    }
}

private fun FlowContent.calendarDay(day: CalendarDayView, messages: UiMessages) {
    val classes = listOf(
        "calendar-day",
        if (day.inCurrentMonth) "current-month" else "outside-month",
        if (day.completionRate == 100) "complete" else "",
    ).filter { it.isNotBlank() }.joinToString(" ")
    div(classes) {
        span("calendar-day-number") { +day.dayNumber.toString() }
        span("calendar-day-progress") {
            +if (day.totalHabits == 0) {
                messages.text("page.calendar.empty")
            } else {
                "${day.completedHabits}/${day.totalHabits}"
            }
        }
    }
}
