package com.example.habittracker.presentation.web.pages

import com.example.habittracker.application.DashboardView
import com.example.habittracker.domain.HabitFrequency
import com.example.habittracker.domain.HabitVisibility
import com.example.habittracker.presentation.web.i18n.UiMessages
import com.example.habittracker.presentation.web.layout.renderPage
import kotlinx.html.ButtonType
import kotlinx.html.FormMethod
import kotlinx.html.InputType
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.hiddenInput
import kotlinx.html.input
import kotlinx.html.label
import kotlinx.html.option
import kotlinx.html.p
import kotlinx.html.section
import kotlinx.html.select
import kotlinx.html.span
import kotlinx.html.textArea

@Suppress("LongMethod")
fun habitPlacePage(
    dashboard: DashboardView,
    csrf: CsrfView?,
    messages: UiMessages,
): String = renderPage(messages.text("page.habitPlace.title"), "Habit Place", messages, true, csrf) {
    habitPlaceHeading(messages)

    section("habit-place-grid") {
        form(action = "/habits", method = FormMethod.post, classes = "form-panel") {
            csrfInput(csrf)
            h2 { +messages.text("page.habitPlace.create") }
            label {
                +messages.text("form.habitName")
                input(type = InputType.text, name = "name") {
                    placeholder = "Example: Study Kotlin"
                    required = true
                }
            }
            label {
                +messages.text("form.category")
                select {
                    name = "category"
                    option { +"Health" }
                    option { +"Learning" }
                    option { +"Work" }
                    option { +"Mindfulness" }
                }
            }
            label {
                +messages.text("form.frequency")
                select {
                    name = "frequency"
                    HabitFrequency.entries.forEach { frequency ->
                        option {
                            value = frequency.name
                            +frequency.label()
                        }
                    }
                }
            }
            label {
                +messages.text("form.goal")
                input(type = InputType.text, name = "goal") {
                    placeholder = "Example: 30 minutes"
                    required = true
                }
            }
            label {
                +messages.text("form.visibility")
                select {
                    name = "visibility"
                    HabitVisibility.entries.forEach { visibility ->
                        option {
                            value = visibility.name
                            +visibility.label()
                        }
                    }
                }
            }
            label {
                +messages.text("form.steps")
                textArea(rows = "4", classes = "textarea") {
                    name = "steps"
                    placeholder = "Optional: one step per line"
                }
            }
            button(classes = "button primary full", type = ButtonType.submit) {
                +messages.text("page.habitPlace.addToWorkspace")
            }
        }

        div("suggestion-grid") {
            dashboard.suggestedHabits.forEach { habit ->
                div("suggestion-card") {
                    span("category-chip") { +habit.category }
                    h2 { +habit.name }
                    p { +"${habit.frequency.label()} - ${habit.goal}" }
                    if (habit.steps.isNotEmpty()) {
                        p("muted") { +habit.steps.joinToString(" / ") }
                    }
                    form(action = "/habits/suggested", method = FormMethod.post) {
                        csrfInput(csrf)
                        hiddenInput(name = "suggestionId") {
                            value = habit.id.toString()
                        }
                        button(classes = "button secondary full", type = ButtonType.submit) {
                            +messages.text("page.habitPlace.addHabit")
                        }
                    }
                }
            }
        }
    }
}

private fun kotlinx.html.FlowContent.habitPlaceHeading(messages: UiMessages) {
    section("page-heading") {
        span("eyebrow") { +messages.text("page.habitPlace.eyebrow") }
        h1 { +messages.text("page.habitPlace.heading") }
        p { +messages.text("page.habitPlace.subtitle") }
    }
}

fun HabitFrequency.label(): String = name.lowercase().replaceFirstChar { it.uppercase() }

fun HabitVisibility.label(): String = name.lowercase().replaceFirstChar { it.uppercase() }
