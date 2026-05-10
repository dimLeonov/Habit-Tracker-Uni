@file:Suppress("CognitiveComplexMethod", "MaxLineLength", "MaximumLineLength")

package com.example.habittracker.presentation.web.pages

import com.example.habittracker.application.DashboardView
import com.example.habittracker.application.HabitStatusView
import com.example.habittracker.application.HabitView
import com.example.habittracker.application.StatView
import com.example.habittracker.domain.HabitFrequency
import com.example.habittracker.domain.HabitVisibility
import com.example.habittracker.presentation.web.i18n.UiMessages
import com.example.habittracker.presentation.web.layout.renderPage
import kotlinx.html.ButtonType
import kotlinx.html.FlowContent
import kotlinx.html.FormMethod
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

fun workspacePage(
    dashboard: DashboardView,
    csrf: CsrfView?,
    messages: UiMessages,
): String = renderPage(
    messages.text("page.workspace.title"),
    "Workspace",
    messages,
    true,
    csrf,
) {
    workspaceHeading(dashboard, messages)
    statsGrid(dashboard.stats)
    habitBoard(dashboard.habits, csrf, messages)
}

fun FlowContent.statsGrid(stats: List<StatView>) {
    section("stats-grid") {
        stats.forEach { stat ->
            statCard(stat)
        }
    }
}

private fun FlowContent.workspaceHeading(dashboard: DashboardView, messages: UiMessages) {
    section("page-heading workspace-heading") {
        span("eyebrow") { +messages.text("page.workspace.eyebrow") }
        h1 { +messages.text("page.workspace.heading", dashboard.userName) }
        p { +messages.text("page.workspace.subtitle") }
    }
}

private fun FlowContent.statCard(stat: StatView) {
    div("stat-card") {
        span("stat-label") { +stat.label }
        span("stat-value") { +stat.value }
        span("stat-detail") { +stat.detail }
    }
}

private fun FlowContent.habitBoard(
    habits: List<HabitView>,
    csrf: CsrfView?,
    messages: UiMessages,
) {
    section("habit-board") {
        habits.forEach { habit ->
            habitCard(habit, csrf, messages)
        }
    }
}

@Suppress("LongMethod")
private fun FlowContent.habitCard(
    habit: HabitView,
    csrf: CsrfView?,
    messages: UiMessages,
) {
    div("habit-card") {
        habitCardHeader(habit, messages)
        div("habit-metrics") {
            span { +messages.text("metric.dayStreak", habit.streakDays) }
            span { +messages.text("metric.consistency", habit.completionRate) }
            span { +messages.text("metric.visibility", habit.visibility.label()) }
        }
        div("week-row") {
            habit.weeklyMarks.forEach { completed ->
                span(if (completed) "week-dot complete" else "week-dot") {}
            }
        }
        if (habit.steps.isEmpty()) {
            postButton("/workspace/habits/${habit.id}/done", csrf, "button primary full") {
                +if (habit.status == HabitStatusView.DONE) {
                    messages.text("action.completedToday")
                } else {
                    messages.text("action.markDone")
                }
            }
        } else {
            div("step-list") {
                habit.steps.forEach { step ->
                    form(
                        action = "/workspace/habits/${habit.id}/steps/${step.id}",
                        method = FormMethod.post,
                        classes = "step-row",
                    ) {
                        csrfInput(csrf)
                        hiddenInput(name = "completed") {
                            value = (!step.completedToday).toString()
                        }
                        span { +step.name }
                        button(classes = "button secondary", type = ButtonType.submit) {
                            +if (step.completedToday) messages.text("action.undo") else messages.text("action.done")
                        }
                    }
                }
            }
        }
        habitActions(habit, csrf, messages)
        editHabitForm(habit, csrf, messages)
    }
}

private fun FlowContent.habitCardHeader(habit: HabitView, messages: UiMessages) {
    div("habit-card-header") {
        div {
            span("category-chip") { +habit.category }
            h2 { +habit.name }
        }
        span("status-pill ${habit.status.name.lowercase()}") {
            +if (habit.status == HabitStatusView.DONE) {
                messages.text("action.done")
            } else {
                messages.text("action.open")
            }
        }
    }
}

private fun FlowContent.habitActions(
    habit: HabitView,
    csrf: CsrfView?,
    messages: UiMessages,
) {
    div("habit-actions") {
        postButton("/workspace/habits/${habit.id}/visibility", csrf, "button secondary") {
            +if (habit.visibility == HabitVisibility.PUBLIC) {
                messages.text("action.makePrivate")
            } else {
                messages.text("action.publish")
            }
        }
        postButton("/workspace/habits/${habit.id}/delete", csrf, "button danger") {
            +messages.text("action.delete")
        }
    }
}

@Suppress("LongMethod")
private fun FlowContent.editHabitForm(
    habit: HabitView,
    csrf: CsrfView?,
    messages: UiMessages,
) {
    form(
        action = "/workspace/habits/${habit.id}",
        method = FormMethod.post,
        classes = "edit-habit-form",
    ) {
        csrfInput(csrf)
        label {
            +messages.text("form.name")
            input(type = kotlinx.html.InputType.text, name = "name") {
                value = habit.name
                required = true
            }
        }
        label {
            +messages.text("form.category")
            input(type = kotlinx.html.InputType.text, name = "category") {
                value = habit.category
                required = true
            }
        }
        label {
            +messages.text("form.frequency")
            select {
                name = "frequency"
                HabitFrequency.entries.forEach { frequency ->
                    option {
                        value = frequency.name
                        selected = frequency == habit.frequency
                        +frequency.label()
                    }
                }
            }
        }
        label {
            +messages.text("form.goal")
            input(type = kotlinx.html.InputType.text, name = "goal") {
                value = habit.goal
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
                        selected = visibility == habit.visibility
                        +visibility.label()
                    }
                }
            }
        }
        label {
            +messages.text("form.steps")
            textArea(rows = "3") {
                name = "steps"
                +habit.steps.joinToString("\n") { it.name }
            }
        }
        button(classes = "button secondary full", type = ButtonType.submit) {
            +messages.text("form.save")
        }
    }
}

private fun FlowContent.postButton(
    action: String,
    csrf: CsrfView?,
    classes: String,
    text: FlowContent.() -> Unit,
) {
    form(action = action, method = FormMethod.post, classes = "inline-form") {
        csrfInput(csrf)
        button(classes = classes, type = ButtonType.submit) {
            text()
        }
    }
}
