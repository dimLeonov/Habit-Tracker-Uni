@file:Suppress("MagicNumber")

package com.example.habittracker.infrastructure.persistence

import com.example.habittracker.domain.HabitFrequency
import com.example.habittracker.domain.HabitVisibility
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDate

@Entity
@Table(
    name = "app_users",
    uniqueConstraints = [UniqueConstraint(columnNames = ["username"])],
)
open class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null,
    @Column(nullable = false, unique = true)
    open var username: String = "",
    @Column(nullable = false)
    open var passwordHash: String = "",
)

@Entity
@Table(name = "workspaces")
open class WorkspaceEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null,
    @Column(nullable = false)
    open var name: String = "Default workspace",
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    open var owner: UserEntity = UserEntity(),
)

@Entity
@Table(name = "habits")
open class HabitEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null,
    @Column(nullable = false)
    open var name: String = "",
    @Column(nullable = false)
    open var category: String = "",
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    open var frequency: HabitFrequency = HabitFrequency.DAILY,
    @Column(nullable = false)
    open var goal: String = "",
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    open var visibility: HabitVisibility = HabitVisibility.PRIVATE,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    open var workspace: WorkspaceEntity = WorkspaceEntity(),
    @OneToMany(mappedBy = "habit", cascade = [CascadeType.ALL], orphanRemoval = true)
    open var steps: MutableList<HabitStepEntity> = mutableListOf(),
)

@Entity
@Table(name = "habit_steps")
open class HabitStepEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null,
    @Column(nullable = false)
    open var title: String = "",
    @Column(nullable = false)
    open var position: Int = 0,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    open var habit: HabitEntity = HabitEntity(),
)

@Entity
@Table(
    name = "habit_completions",
    uniqueConstraints = [UniqueConstraint(columnNames = ["habit_id", "completedOn"])],
)
open class HabitCompletionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    open var habit: HabitEntity = HabitEntity(),
    @Column(nullable = false)
    open var completedOn: LocalDate = LocalDate.now(),
)

@Entity
@Table(
    name = "step_completions",
    uniqueConstraints = [UniqueConstraint(columnNames = ["step_id", "completedOn"])],
)
open class StepCompletionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    open var step: HabitStepEntity = HabitStepEntity(),
    @Column(nullable = false)
    open var completedOn: LocalDate = LocalDate.now(),
)

@Entity
@Table(name = "suggested_habits")
open class SuggestedHabitEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null,
    @Column(nullable = false)
    open var name: String = "",
    @Column(nullable = false)
    open var category: String = "",
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    open var frequency: HabitFrequency = HabitFrequency.DAILY,
    @Column(nullable = false)
    open var goal: String = "",
    @OneToMany(mappedBy = "suggestion", cascade = [CascadeType.ALL], orphanRemoval = true)
    open var steps: MutableList<SuggestedHabitStepEntity> = mutableListOf(),
)

@Entity
@Table(name = "suggested_habit_steps")
open class SuggestedHabitStepEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null,
    @Column(nullable = false)
    open var title: String = "",
    @Column(nullable = false)
    open var position: Int = 0,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    open var suggestion: SuggestedHabitEntity = SuggestedHabitEntity(),
)
