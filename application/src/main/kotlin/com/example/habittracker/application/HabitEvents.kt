package com.example.habittracker.application

import java.time.LocalDate

interface HabitEvent

interface HabitEventListener {
    fun handle(event: HabitEvent)
}

interface HabitEventPublisher {
    fun publish(event: HabitEvent)
}

data class HabitCompletedEvent(
    val username: String,
    val habitId: Long,
    val completedOn: LocalDate,
) : HabitEvent

class CompositeHabitEventPublisher(private val listeners: List<HabitEventListener>) : HabitEventPublisher {
    override fun publish(event: HabitEvent) {
        listeners.forEach { it.handle(event) }
    }
}
