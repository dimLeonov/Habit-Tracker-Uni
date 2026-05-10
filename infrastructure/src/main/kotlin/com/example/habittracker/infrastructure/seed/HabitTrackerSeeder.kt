@file:Suppress("MagicNumber")

package com.example.habittracker.infrastructure.seed

import com.example.habittracker.domain.HabitFrequency
import com.example.habittracker.domain.HabitVisibility
import com.example.habittracker.infrastructure.persistence.HabitCompletionEntity
import com.example.habittracker.infrastructure.persistence.HabitCompletionRepository
import com.example.habittracker.infrastructure.persistence.HabitEntity
import com.example.habittracker.infrastructure.persistence.HabitRepository
import com.example.habittracker.infrastructure.persistence.HabitStepEntity
import com.example.habittracker.infrastructure.persistence.StepCompletionEntity
import com.example.habittracker.infrastructure.persistence.StepCompletionRepository
import com.example.habittracker.infrastructure.persistence.SuggestedHabitEntity
import com.example.habittracker.infrastructure.persistence.SuggestedHabitRepository
import com.example.habittracker.infrastructure.persistence.SuggestedHabitStepEntity
import com.example.habittracker.infrastructure.persistence.UserEntity
import com.example.habittracker.infrastructure.persistence.UserRepository
import com.example.habittracker.infrastructure.persistence.WorkspaceEntity
import com.example.habittracker.infrastructure.persistence.WorkspaceRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Component
open class HabitTrackerSeeder(
    private val users: UserRepository,
    private val workspaces: WorkspaceRepository,
    private val habits: HabitRepository,
    private val habitCompletions: HabitCompletionRepository,
    private val stepCompletions: StepCompletionRepository,
    private val suggestions: SuggestedHabitRepository,
    private val passwordEncoder: PasswordEncoder,
) : CommandLineRunner {
    @Transactional
    override fun run(vararg args: String) {
        if (users.count() > 0) {
            return
        }
        seedSuggestions()
        seedUser("dmitry", "Morning walk", 28, HabitVisibility.PUBLIC)
        seedUser("marta", "Hydration reset", 36, HabitVisibility.PUBLIC)
        seedUser("anna", "Read 20 pages", 18, HabitVisibility.PUBLIC)
        seedUser("rihards", "Study Kotlin", 12, HabitVisibility.PRIVATE)
        seedUser("sofia", "Evening stretch", 9, HabitVisibility.PUBLIC)
    }

    private fun seedSuggestions() {
        suggestions.save(suggestion("Drink water", "Health", HabitFrequency.DAILY, "8 glasses"))
        suggestions.save(suggestion("Plan tomorrow", "Focus", HabitFrequency.DAILY, "5 minutes"))
        suggestions.save(suggestion("Deep work block", "Work", HabitFrequency.WEEKDAYS, "90 minutes"))
        suggestions.save(
            suggestion(
                "Evening reset",
                "Wellness",
                HabitFrequency.DAILY,
                "3 small steps",
                listOf("Prepare clothes", "Clear desk", "Set alarm"),
            ),
        )
        suggestions.save(
            suggestion(
                "Study session",
                "Learning",
                HabitFrequency.DAILY,
                "45 minutes",
                listOf("Review notes", "Practice task", "Write summary"),
            ),
        )
    }

    @Suppress("LongMethod")
    private fun seedUser(
        username: String,
        mainHabitName: String,
        streakDays: Int,
        visibility: HabitVisibility,
    ) {
        val user = users.save(
            UserEntity(
                username = username,
                passwordHash = requireNotNull(passwordEncoder.encode("password")),
            ),
        )
        val workspace = workspaces.save(WorkspaceEntity(owner = user))
        val mainHabit = habits.save(
            HabitEntity(
                name = mainHabitName,
                category = "Health",
                frequency = HabitFrequency.DAILY,
                goal = "Daily routine",
                visibility = visibility,
                workspace = workspace,
            ),
        )
        val advancedHabit = HabitEntity(
            name = "Focus block",
            category = "Work",
            frequency = HabitFrequency.WEEKDAYS,
            goal = "60 minutes",
            visibility = HabitVisibility.PUBLIC,
            workspace = workspace,
        )
        listOf("Set timer", "Work without phone", "Log outcome").forEachIndexed { index, title ->
            advancedHabit.steps.add(HabitStepEntity(title = title, position = index, habit = advancedHabit))
        }
        val savedAdvancedHabit = habits.save(advancedHabit)
        seedCompletions(mainHabit, streakDays)
        seedCompletions(savedAdvancedHabit, streakDays / 2)
        seedStepCompletions(savedAdvancedHabit, streakDays / 2)
    }

    private fun seedCompletions(habit: HabitEntity, days: Int) {
        val today = LocalDate.now()
        repeat(days) { index ->
            habitCompletions.save(HabitCompletionEntity(habit = habit, completedOn = today.minusDays(index.toLong())))
        }
    }

    private fun seedStepCompletions(habit: HabitEntity, days: Int) {
        val today = LocalDate.now()
        repeat(days) { index ->
            val date = today.minusDays(index.toLong())
            habit.steps.forEach { step ->
                stepCompletions.save(StepCompletionEntity(step = step, completedOn = date))
            }
        }
    }

    private fun suggestion(
        name: String,
        category: String,
        frequency: HabitFrequency,
        goal: String,
        stepTitles: List<String> = emptyList(),
    ): SuggestedHabitEntity {
        val suggestion = SuggestedHabitEntity(
            name = name,
            category = category,
            frequency = frequency,
            goal = goal,
        )
        stepTitles.forEachIndexed { index, title ->
            suggestion.steps.add(SuggestedHabitStepEntity(title = title, position = index, suggestion = suggestion))
        }
        return suggestion
    }
}
