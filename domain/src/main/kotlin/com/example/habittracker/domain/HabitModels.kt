package com.example.habittracker.domain

enum class HabitFrequency {
    DAILY,
    WEEKDAYS,
    WEEKLY,
}

enum class HabitVisibility {
    PRIVATE,
    PUBLIC,
}

data class HabitDraft(
    val name: String,
    val category: String,
    val frequency: HabitFrequency,
    val goal: String,
    val visibility: HabitVisibility,
    val steps: List<String>,
) {
    class Builder {
        private var name: String = ""
        private var category: String = ""
        private var frequency: HabitFrequency = HabitFrequency.DAILY
        private var goal: String = ""
        private var visibility: HabitVisibility = HabitVisibility.PRIVATE
        private val steps: MutableList<String> = mutableListOf()

        fun named(value: String) = apply {
            name = value.trim()
        }

        fun inCategory(value: String) = apply {
            category = value.trim()
        }

        fun scheduledAs(value: HabitFrequency) = apply {
            frequency = value
        }

        fun withGoal(value: String) = apply {
            goal = value.trim()
        }

        fun visibleAs(value: HabitVisibility) = apply {
            visibility = value
        }

        fun withSteps(values: Iterable<String>) = apply {
            steps.clear()
            steps.addAll(values.map { it.trim() }.filter { it.isNotBlank() })
        }

        fun build(): HabitDraft {
            require(name.isNotBlank()) { "Habit name is required." }
            require(category.isNotBlank()) { "Habit category is required." }
            require(goal.isNotBlank()) { "Habit goal is required." }
            return HabitDraft(
                name = name,
                category = category,
                frequency = frequency,
                goal = goal,
                visibility = visibility,
                steps = steps.toList(),
            )
        }
    }
}
