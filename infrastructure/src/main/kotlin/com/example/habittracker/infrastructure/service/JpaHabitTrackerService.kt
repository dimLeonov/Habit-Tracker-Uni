@file:Suppress("MagicNumber", "TooManyFunctions")

package com.example.habittracker.infrastructure.service

import com.example.habittracker.application.CalendarDayView
import com.example.habittracker.application.CalendarView
import com.example.habittracker.application.CalendarWeekView
import com.example.habittracker.application.DashboardView
import com.example.habittracker.application.HabitCalendarService
import com.example.habittracker.application.HabitCompletedEvent
import com.example.habittracker.application.HabitCreateCommand
import com.example.habittracker.application.HabitDraftFactory
import com.example.habittracker.application.HabitEventPublisher
import com.example.habittracker.application.HabitProgressReporter
import com.example.habittracker.application.HabitStatusView
import com.example.habittracker.application.HabitStepView
import com.example.habittracker.application.HabitSuggestionSource
import com.example.habittracker.application.HabitTrackerService
import com.example.habittracker.application.HabitUpdateCommand
import com.example.habittracker.application.HabitView
import com.example.habittracker.application.LeaderboardEntryView
import com.example.habittracker.application.LeaderboardParticipant
import com.example.habittracker.application.LeaderboardRanker
import com.example.habittracker.application.RECENT_WINDOW_DAYS
import com.example.habittracker.application.StatView
import com.example.habittracker.application.SuggestedHabitView
import com.example.habittracker.domain.HabitDraft
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
import com.example.habittracker.infrastructure.persistence.UserEntity
import com.example.habittracker.infrastructure.persistence.UserRepository
import com.example.habittracker.infrastructure.persistence.WorkspaceEntity
import com.example.habittracker.infrastructure.persistence.WorkspaceRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.TemporalAdjusters

private const val DEFAULT_WORKSPACE_NAME = "Default workspace"
private const val WEEK_LENGTH = 7

@Service
open class JpaHabitTrackerService(
    private val users: UserRepository,
    private val workspaces: WorkspaceRepository,
    private val habits: HabitRepository,
    private val habitCompletions: HabitCompletionRepository,
    private val stepCompletions: StepCompletionRepository,
    private val suggestions: SuggestedHabitRepository,
    private val passwordEncoder: PasswordEncoder,
    private val habitDraftFactory: HabitDraftFactory,
    private val progressReporter: HabitProgressReporter,
    private val leaderboardRanker: LeaderboardRanker,
    private val eventPublisher: HabitEventPublisher,
) : HabitTrackerService,
    HabitCalendarService {
    @Transactional(readOnly = true)
    override fun publicDashboard(): DashboardView {
        val publicHabits = habits.findAllByVisibilityOrderById(HabitVisibility.PUBLIC)
        return DashboardView(
            userName = "Guest",
            stats = publicStats(publicHabits),
            habits = publicHabits.take(4).map { it.toView(LocalDate.now()) },
            suggestedHabits = suggestions.findAll().map { it.toView() },
            leaderboard = leaderboard(),
        )
    }

    @Transactional(readOnly = true)
    override fun dashboardFor(username: String): DashboardView {
        val today = LocalDate.now()
        val userHabits = habits.findAllByWorkspaceOwnerUsernameOrderById(username)
        return DashboardView(
            userName = username,
            stats = userStats(username, userHabits, today),
            habits = userHabits.map { it.toView(today) },
            suggestedHabits = suggestions.findAll().map { it.toView() },
            leaderboard = leaderboard(),
        )
    }

    @Transactional(readOnly = true)
    override fun calendarFor(username: String, month: String?): CalendarView {
        val selectedMonth = month?.let { YearMonth.parse(it) } ?: YearMonth.now()
        val userHabits = habits.findAllByWorkspaceOwnerUsernameOrderById(username)
        val firstGridDate = selectedMonth
            .atDay(1)
            .with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))
        val lastGridDate = selectedMonth
            .atEndOfMonth()
            .with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY))
        val completionsByDate = completionsByDate(userHabits, firstGridDate, lastGridDate)
        val days = generateSequence(firstGridDate) { date ->
            date.plusDays(1).takeIf { !it.isAfter(lastGridDate) }
        }.map { date ->
            val completedHabits = completionsByDate[date] ?: 0
            CalendarDayView(
                date = date.toString(),
                dayNumber = date.dayOfMonth,
                inCurrentMonth = YearMonth.from(date) == selectedMonth,
                completedHabits = completedHabits,
                totalHabits = userHabits.size,
                completionRate = completionRate(completedHabits, userHabits.size),
            )
        }.toList()
        return CalendarView(
            userName = username,
            month = selectedMonth.toString(),
            previousMonth = selectedMonth.minusMonths(1).toString(),
            nextMonth = selectedMonth.plusMonths(1).toString(),
            completedDays = days.filter { it.inCurrentMonth }.sumOf { it.completedHabits },
            totalHabitDays = days.count { it.inCurrentMonth } * userHabits.size,
            weeks = days.chunked(WEEK_LENGTH).map { CalendarWeekView(it) },
        )
    }

    @Transactional
    override fun register(username: String, password: String) {
        val normalizedUsername = username.trim()
        require(normalizedUsername.length >= 3) { "Username must contain at least 3 characters." }
        require(password.length >= 6) { "Password must contain at least 6 characters." }
        if (users.existsByUsername(normalizedUsername)) {
            throw DataIntegrityViolationException("Username is already taken.")
        }
        val user = users.save(
            UserEntity(
                username = normalizedUsername,
                passwordHash = requireNotNull(passwordEncoder.encode(password)),
            ),
        )
        workspaces.save(WorkspaceEntity(name = DEFAULT_WORKSPACE_NAME, owner = user))
    }

    @Transactional
    override fun createHabit(username: String, command: HabitCreateCommand) {
        val workspace = workspaceFor(username)
        habits.save(habitDraftFactory.from(command).toEntity(workspace))
    }

    @Transactional
    override fun addSuggestedHabit(username: String, suggestionId: Long) {
        val workspace = workspaceFor(username)
        val suggestion = suggestions.findById(suggestionId).orElseThrow {
            IllegalArgumentException("Suggested habit was not found.")
        }
        habits.save(habitDraftFactory.privateCopyOf(suggestion.toSource()).toEntity(workspace))
    }

    @Transactional
    override fun updateHabit(
        username: String,
        habitId: Long,
        command: HabitUpdateCommand,
    ) {
        val habit = habitFor(username, habitId)
        val draft = habitDraftFactory.from(command)
        habit.name = draft.name
        habit.category = draft.category
        habit.frequency = draft.frequency
        habit.goal = draft.goal
        habit.visibility = draft.visibility
        habit.steps.clear()
        draft.steps.forEachIndexed { index, title ->
            habit.steps.add(HabitStepEntity(title = title, position = index, habit = habit))
        }
    }

    @Transactional
    override fun deleteHabit(username: String, habitId: Long) {
        habits.delete(habitFor(username, habitId))
    }

    @Transactional
    override fun toggleHabitVisibility(username: String, habitId: Long) {
        val habit = habitFor(username, habitId)
        habit.visibility = if (habit.visibility == HabitVisibility.PUBLIC) {
            HabitVisibility.PRIVATE
        } else {
            HabitVisibility.PUBLIC
        }
    }

    @Transactional
    override fun markHabitDone(username: String, habitId: Long) {
        val habit = habitFor(username, habitId)
        val today = LocalDate.now()
        habit.steps.forEach { step ->
            val stepId = requireNotNull(step.id)
            if (!stepCompletions.existsByStepIdAndCompletedOn(stepId, today)) {
                stepCompletions.save(StepCompletionEntity(step = step, completedOn = today))
            }
        }
        completeHabitIfMissing(username, habit, today)
    }

    @Transactional
    override fun setStepCompletion(
        username: String,
        habitId: Long,
        stepId: Long,
        completed: Boolean,
    ) {
        val habit = habitFor(username, habitId)
        val step = habit.steps.firstOrNull { it.id == stepId }
            ?: throw IllegalArgumentException("Habit step was not found.")
        val today = LocalDate.now()
        val existing = stepCompletions.findByStepIdAndCompletedOn(stepId, today)

        if (completed && existing == null) {
            stepCompletions.save(StepCompletionEntity(step = step, completedOn = today))
        }

        if (!completed && existing != null) {
            stepCompletions.delete(existing)
        }

        if (habit.steps.all { stepCompletions.existsByStepIdAndCompletedOn(requireNotNull(it.id), today) }) {
            completeHabitIfMissing(username, habit, today)
        } else {
            habitCompletions.deleteByHabitIdAndCompletedOn(habitId, today)
        }
    }

    private fun workspaceFor(username: String): WorkspaceEntity =
        workspaces.findByOwnerUsername(username) ?: throw IllegalStateException("Workspace was not found.")

    private fun habitFor(username: String, habitId: Long): HabitEntity =
        habits.findByIdAndWorkspaceOwnerUsername(habitId, username)
            ?: throw IllegalArgumentException("Habit was not found.")

    private fun HabitDraft.toEntity(workspace: WorkspaceEntity): HabitEntity {
        val habit = HabitEntity(
            name = name,
            category = category,
            frequency = frequency,
            goal = goal,
            visibility = visibility,
            workspace = workspace,
        )
        steps.forEachIndexed { index, title ->
            habit.steps.add(HabitStepEntity(title = title, position = index, habit = habit))
        }
        return habit
    }

    private fun SuggestedHabitEntity.toSource(): HabitSuggestionSource = SuggestedHabitSource(
        name = name,
        category = category,
        frequency = frequency,
        goal = goal,
        steps = steps.sortedBy { it.position }.map { it.title },
    )

    private fun HabitEntity.toView(today: LocalDate): HabitView {
        val habitId = requireNotNull(id)
        return HabitView(
            id = habitId,
            name = name,
            category = category,
            frequency = frequency,
            goal = goal,
            streakDays = currentStreak(this, today),
            completionRate = completionRate(this, today),
            status = if (isCompletedToday(habitId, today)) HabitStatusView.DONE else HabitStatusView.PENDING,
            visibility = visibility,
            weeklyMarks = weekMarks(this, today),
            steps = steps.sortedBy { it.position }.map { step ->
                HabitStepView(
                    id = requireNotNull(step.id),
                    name = step.title,
                    completedToday = stepCompletions.existsByStepIdAndCompletedOn(requireNotNull(step.id), today),
                )
            },
        )
    }

    private fun SuggestedHabitEntity.toView(): SuggestedHabitView = SuggestedHabitView(
        id = requireNotNull(id),
        name = name,
        category = category,
        frequency = frequency,
        goal = goal,
        steps = steps.sortedBy { it.position }.map { it.title },
    )

    private fun publicStats(publicHabits: List<HabitEntity>): List<StatView> = listOf(
        StatView("Published habits", publicHabits.size.toString(), "Visible to the community"),
        StatView("Active users", users.count().toString(), "Seeded and registered users"),
        StatView("Suggested habits", suggestions.count().toString(), "Ready to add"),
        StatView("Leaderboard", "${leaderboard().size} users", "Ranked by consistency"),
    )

    private fun userStats(
        username: String,
        userHabits: List<HabitEntity>,
        today: LocalDate,
    ): List<StatView> {
        val completedToday = userHabits.count { isCompletedToday(requireNotNull(it.id), today) }
        val bestStreak = userHabits.maxOfOrNull { currentStreak(it, today) } ?: 0
        val consistency = if (userHabits.isEmpty()) {
            0
        } else {
            userHabits.map { completionRate(it, today) }.average().toInt()
        }
        val completedThisWeek = habitCompletions.countByHabitWorkspaceOwnerUsernameAndCompletedOnBetween(
            username,
            today.minusDays(RECENT_WINDOW_DAYS),
            today,
        )
        return listOf(
            StatView("Completed today", "$completedToday/${userHabits.size}", "Open habits stay visible"),
            StatView("Best streak", "$bestStreak days", "Across your workspace"),
            StatView("Consistency", "$consistency%", "Last 30 days"),
            StatView("This week", completedThisWeek.toString(), "Completed habit days"),
        )
    }

    private fun leaderboard(): List<LeaderboardEntryView> {
        val today = LocalDate.now()
        val entries = users.findAll().map { user ->
            val username = user.username
            val userHabits = habits.findAllByWorkspaceOwnerUsernameOrderById(username)
            val completedThisWeek = habitCompletions.countByHabitWorkspaceOwnerUsernameAndCompletedOnBetween(
                username,
                today.minusDays(RECENT_WINDOW_DAYS),
                today,
            )
            LeaderboardParticipant(
                name = username,
                completedThisWeek = completedThisWeek,
                bestStreak = userHabits.maxOfOrNull { currentStreak(it, today) } ?: 0,
            )
        }
        return leaderboardRanker.rank(entries)
    }

    private fun weekMarks(habit: HabitEntity, today: LocalDate): List<Boolean> {
        val habitId = requireNotNull(habit.id)
        return progressReporter.weekMarks(habit.frequency, today) { date -> isCompletedToday(habitId, date) }
    }

    private fun completionRate(habit: HabitEntity, today: LocalDate): Int {
        val habitId = requireNotNull(habit.id)
        return progressReporter.completionRate(habit.frequency, today) { date -> isCompletedToday(habitId, date) }
    }

    private fun currentStreak(habit: HabitEntity, today: LocalDate): Int {
        val habitId = requireNotNull(habit.id)
        return progressReporter.currentStreak(habit.frequency, today) { date -> isCompletedToday(habitId, date) }
    }

    private fun isCompletedToday(habitId: Long, date: LocalDate): Boolean =
        habitCompletions.existsByHabitIdAndCompletedOn(habitId, date)

    private fun completionsByDate(
        userHabits: List<HabitEntity>,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Map<LocalDate, Int> {
        val habitIds = userHabits.mapNotNull { it.id }
        if (habitIds.isEmpty()) {
            return emptyMap()
        }
        return habitCompletions
            .findAllByHabitIdInAndCompletedOnBetween(habitIds, startDate, endDate)
            .groupingBy { it.completedOn }
            .eachCount()
    }

    private fun completionRate(completedHabits: Int, totalHabits: Int): Int =
        if (totalHabits == 0) 0 else (completedHabits.toDouble() / totalHabits * 100).toInt()

    private fun completeHabitIfMissing(
        username: String,
        habit: HabitEntity,
        today: LocalDate,
    ) {
        val habitId = requireNotNull(habit.id)
        if (!habitCompletions.existsByHabitIdAndCompletedOn(habitId, today)) {
            habitCompletions.save(HabitCompletionEntity(habit = habit, completedOn = today))
            eventPublisher.publish(HabitCompletedEvent(username = username, habitId = habitId, completedOn = today))
        }
    }
}

private data class SuggestedHabitSource(
    override val name: String,
    override val category: String,
    override val frequency: HabitFrequency,
    override val goal: String,
    override val steps: List<String>,
) : HabitSuggestionSource
