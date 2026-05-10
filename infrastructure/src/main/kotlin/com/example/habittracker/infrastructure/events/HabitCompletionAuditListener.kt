package com.example.habittracker.infrastructure.events

import com.example.habittracker.application.HabitCompletedEvent
import com.example.habittracker.application.HabitEvent
import com.example.habittracker.application.HabitEventListener
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class HabitCompletionAuditListener : HabitEventListener {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun handle(event: HabitEvent) {
        if (event is HabitCompletedEvent) {
            logger.info("Habit {} completed by {} on {}", event.habitId, event.username, event.completedOn)
        }
    }
}
