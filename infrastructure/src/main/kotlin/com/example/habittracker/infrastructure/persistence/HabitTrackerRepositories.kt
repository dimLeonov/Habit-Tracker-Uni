package com.example.habittracker.infrastructure.persistence

import com.example.habittracker.domain.HabitVisibility
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByUsername(username: String): UserEntity?
    fun existsByUsername(username: String): Boolean
}

interface WorkspaceRepository : JpaRepository<WorkspaceEntity, Long> {
    fun findByOwnerUsername(username: String): WorkspaceEntity?
}

interface HabitRepository : JpaRepository<HabitEntity, Long> {
    fun findAllByWorkspaceOwnerUsernameOrderById(username: String): List<HabitEntity>
    fun findByIdAndWorkspaceOwnerUsername(id: Long, username: String): HabitEntity?
    fun findAllByVisibilityOrderById(visibility: HabitVisibility): List<HabitEntity>
}

interface HabitStepRepository : JpaRepository<HabitStepEntity, Long>

interface HabitCompletionRepository : JpaRepository<HabitCompletionEntity, Long> {
    fun findAllByHabitIdInAndCompletedOnBetween(
        habitIds: Collection<Long>,
        startDate: LocalDate,
        endDate: LocalDate,
    ): List<HabitCompletionEntity>

    fun existsByHabitIdAndCompletedOn(habitId: Long, completedOn: LocalDate): Boolean
    fun deleteByHabitIdAndCompletedOn(habitId: Long, completedOn: LocalDate)
    fun countByHabitWorkspaceOwnerUsernameAndCompletedOnBetween(
        username: String,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Int
}

interface StepCompletionRepository : JpaRepository<StepCompletionEntity, Long> {
    fun existsByStepIdAndCompletedOn(stepId: Long, completedOn: LocalDate): Boolean
    fun findByStepIdAndCompletedOn(stepId: Long, completedOn: LocalDate): StepCompletionEntity?
}

interface SuggestedHabitRepository : JpaRepository<SuggestedHabitEntity, Long>
