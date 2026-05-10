package com.example.habittracker.application

import com.example.habittracker.domain.CompletionPolicyFactory
import com.example.habittracker.domain.HabitFrequency
import java.time.LocalDate

private const val PERCENT = 100

interface LeaderboardScoringStrategy {
    fun score(participant: LeaderboardParticipant): Int
}

class HabitProgressReporter(private val completionPolicies: CompletionPolicyFactory) {
    fun weekMarks(
        frequency: HabitFrequency,
        today: LocalDate,
        isCompleted: (LocalDate) -> Boolean,
    ): List<Boolean> = completionPolicies
        .policyFor(frequency)
        .trackedDatesEndingOn(today, RECENT_WINDOW_DAYS)
        .map(isCompleted)

    fun completionRate(
        frequency: HabitFrequency,
        today: LocalDate,
        isCompleted: (LocalDate) -> Boolean,
    ): Int {
        val trackedDates = completionPolicies
            .policyFor(frequency)
            .trackedDatesEndingOn(today, CONSISTENCY_WINDOW_DAYS)
        if (trackedDates.isEmpty()) {
            return 0
        }
        return (trackedDates.count(isCompleted).toDouble() / trackedDates.size * PERCENT).toInt()
    }

    fun currentStreak(
        frequency: HabitFrequency,
        today: LocalDate,
        isCompleted: (LocalDate) -> Boolean,
    ): Int = completionPolicies
        .policyFor(frequency)
        .trackedDatesEndingOn(today, CONSISTENCY_WINDOW_DAYS)
        .asReversed()
        .takeWhile(isCompleted)
        .size
}

class LeaderboardRanker(private val scoringStrategy: LeaderboardScoringStrategy = ConsistencyScoringStrategy()) {
    fun rank(participants: List<LeaderboardParticipant>): List<LeaderboardEntryView> {
        val scoreOrder = compareByDescending<LeaderboardEntryView> { it.score }.thenBy { it.name }
        val entries = participants.map { participant ->
            LeaderboardEntryView(
                rank = 0,
                name = participant.name,
                initials = participant.name.initials(),
                score = scoringStrategy.score(participant),
                streakDays = participant.bestStreak,
                completedThisWeek = participant.completedThisWeek,
            )
        }
        return entries.sortedWith(scoreOrder).mapIndexed { index, entry -> entry.copy(rank = index + 1) }
    }
}

class ConsistencyScoringStrategy : LeaderboardScoringStrategy {
    override fun score(participant: LeaderboardParticipant): Int =
        participant.completedThisWeek * WEEK_COMPLETION_POINTS + participant.bestStreak * STREAK_POINTS
}

data class LeaderboardParticipant(
    val name: String,
    val completedThisWeek: Int,
    val bestStreak: Int,
)

const val RECENT_WINDOW_DAYS = 6L
const val CONSISTENCY_WINDOW_DAYS = 29L

private const val WEEK_COMPLETION_POINTS = 100
private const val STREAK_POINTS = 10

private fun String.initials(): String = trim()
    .split(Regex("\\s+"))
    .filter { it.isNotBlank() }
    .take(2)
    .joinToString("") { it.first().uppercase() }
    .ifBlank { "U" }
