package com.example.habittracker.domain

import java.time.DayOfWeek
import java.time.LocalDate

interface CompletionPolicy {
    fun trackedDatesEndingOn(endDate: LocalDate, trackedDays: Long): List<LocalDate>
}

class DailyCompletionPolicy : CompletionPolicy {
    override fun trackedDatesEndingOn(endDate: LocalDate, trackedDays: Long): List<LocalDate> =
        datesEndingOn(endDate, trackedDays)
}

class WeekdayCompletionPolicy : CompletionPolicy {
    override fun trackedDatesEndingOn(endDate: LocalDate, trackedDays: Long): List<LocalDate> =
        datesEndingOn(endDate, trackedDays)
            .filterNot { it.dayOfWeek == DayOfWeek.SATURDAY || it.dayOfWeek == DayOfWeek.SUNDAY }
}

class WeeklyCompletionPolicy : CompletionPolicy {
    override fun trackedDatesEndingOn(endDate: LocalDate, trackedDays: Long): List<LocalDate> =
        datesEndingOn(endDate, trackedDays)
            .filter { it.dayOfWeek == endDate.dayOfWeek }
}

class CompletionPolicyFactory(
    private val policies: Map<HabitFrequency, CompletionPolicy> = mapOf(
        HabitFrequency.DAILY to DailyCompletionPolicy(),
        HabitFrequency.WEEKDAYS to WeekdayCompletionPolicy(),
        HabitFrequency.WEEKLY to WeeklyCompletionPolicy(),
    ),
) {
    fun policyFor(frequency: HabitFrequency): CompletionPolicy =
        requireNotNull(policies[frequency]) { "Completion policy is not configured for $frequency." }
}

private fun datesEndingOn(endDate: LocalDate, trackedDays: Long): List<LocalDate> =
    (trackedDays downTo 0L).map { daysAgo -> endDate.minusDays(daysAgo) }
