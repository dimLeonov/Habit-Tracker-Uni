package com.example.habittracker.application

import com.example.habittracker.domain.HabitFrequency
import com.example.habittracker.domain.HabitVisibility

interface HabitTrackerService {
    fun publicDashboard(): DashboardView
    fun dashboardFor(username: String): DashboardView
    fun register(username: String, password: String)
    fun createHabit(username: String, command: HabitCreateCommand)
    fun addSuggestedHabit(username: String, suggestionId: Long)
    fun updateHabit(
        username: String,
        habitId: Long,
        command: HabitUpdateCommand,
    )
    fun deleteHabit(username: String, habitId: Long)
    fun toggleHabitVisibility(username: String, habitId: Long)
    fun markHabitDone(username: String, habitId: Long)
    fun setStepCompletion(
        username: String,
        habitId: Long,
        stepId: Long,
        completed: Boolean,
    )
}

interface HabitCalendarService {
    fun calendarFor(username: String, month: String? = null): CalendarView
}

data class DashboardView(
    val userName: String,
    val stats: List<StatView>,
    val habits: List<HabitView>,
    val suggestedHabits: List<SuggestedHabitView>,
    val leaderboard: List<LeaderboardEntryView>,
)

data class StatView(
    val label: String,
    val value: String,
    val detail: String,
)

data class HabitView(
    val id: Long,
    val name: String,
    val category: String,
    val frequency: HabitFrequency,
    val goal: String,
    val streakDays: Int,
    val completionRate: Int,
    val status: HabitStatusView,
    val visibility: HabitVisibility,
    val weeklyMarks: List<Boolean>,
    val steps: List<HabitStepView>,
)

data class HabitStepView(
    val id: Long,
    val name: String,
    val completedToday: Boolean,
)

enum class HabitStatusView {
    DONE,
    PENDING,
}

data class SuggestedHabitView(
    val id: Long,
    val name: String,
    val category: String,
    val frequency: HabitFrequency,
    val goal: String,
    val steps: List<String>,
)

data class LeaderboardEntryView(
    val rank: Int,
    val name: String,
    val initials: String,
    val score: Int,
    val streakDays: Int,
    val completedThisWeek: Int,
)

data class CalendarView(
    val userName: String,
    val month: String,
    val previousMonth: String,
    val nextMonth: String,
    val completedDays: Int,
    val totalHabitDays: Int,
    val weeks: List<CalendarWeekView>,
)

data class CalendarWeekView(val days: List<CalendarDayView>)

data class CalendarDayView(
    val date: String,
    val dayNumber: Int,
    val inCurrentMonth: Boolean,
    val completedHabits: Int,
    val totalHabits: Int,
    val completionRate: Int,
)

data class HabitCreateCommand(
    val name: String,
    val category: String,
    val frequency: HabitFrequency,
    val goal: String,
    val visibility: HabitVisibility,
    val steps: List<String>,
)

data class HabitUpdateCommand(
    val name: String,
    val category: String,
    val frequency: HabitFrequency,
    val goal: String,
    val visibility: HabitVisibility,
    val steps: List<String>,
)
