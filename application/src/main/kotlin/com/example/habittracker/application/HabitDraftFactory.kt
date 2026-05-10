package com.example.habittracker.application

import com.example.habittracker.domain.HabitDraft
import com.example.habittracker.domain.HabitVisibility

interface HabitSuggestionSource {
    val name: String
    val category: String
    val frequency: com.example.habittracker.domain.HabitFrequency
    val goal: String
    val steps: List<String>
}

class HabitDraftFactory {
    fun from(command: HabitCreateCommand): HabitDraft = HabitDraft
        .Builder()
        .named(command.name)
        .inCategory(command.category)
        .scheduledAs(command.frequency)
        .withGoal(command.goal)
        .visibleAs(command.visibility)
        .withSteps(command.steps)
        .build()

    fun from(command: HabitUpdateCommand): HabitDraft = HabitDraft
        .Builder()
        .named(command.name)
        .inCategory(command.category)
        .scheduledAs(command.frequency)
        .withGoal(command.goal)
        .visibleAs(command.visibility)
        .withSteps(command.steps)
        .build()

    fun privateCopyOf(suggestion: HabitSuggestionSource): HabitDraft = HabitDraft
        .Builder()
        .named(suggestion.name)
        .inCategory(suggestion.category)
        .scheduledAs(suggestion.frequency)
        .withGoal(suggestion.goal)
        .visibleAs(HabitVisibility.PRIVATE)
        .withSteps(suggestion.steps)
        .build()
}
