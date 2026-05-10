package com.example.habittracker.presentation.web.controllers

import com.example.habittracker.application.HabitTrackerService
import com.example.habittracker.presentation.web.config.API_VERSION_1
import com.example.habittracker.presentation.web.config.API_VERSION_2
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.OffsetDateTime

@RestController
class HabitTrackerApiController(private val habitTracker: HabitTrackerService) {
    @GetMapping(
        "/api/leaderboard",
        version = API_VERSION_1,
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun leaderboardV1(): LeaderboardApiV1Response = LeaderboardApiV1Response(
        entries = habitTracker.publicDashboard().leaderboard.map { entry ->
            LeaderboardApiV1Entry(
                rank = entry.rank,
                name = entry.name,
                score = entry.score,
            )
        },
    )

    @GetMapping(
        "/api/leaderboard",
        version = API_VERSION_2,
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun leaderboardV2(): LeaderboardApiV2Response = LeaderboardApiV2Response(
        generatedAt = OffsetDateTime.now().toString(),
        entries = habitTracker.publicDashboard().leaderboard.map { entry ->
            LeaderboardApiV2Entry(
                rank = entry.rank,
                name = entry.name,
                initials = entry.initials,
                score = entry.score,
                streakDays = entry.streakDays,
                completedThisWeek = entry.completedThisWeek,
            )
        },
    )
}

data class LeaderboardApiV1Response(val entries: List<LeaderboardApiV1Entry>)

data class LeaderboardApiV1Entry(
    val rank: Int,
    val name: String,
    val score: Int,
)

data class LeaderboardApiV2Response(
    val generatedAt: String,
    val entries: List<LeaderboardApiV2Entry>,
)

data class LeaderboardApiV2Entry(
    val rank: Int,
    val name: String,
    val initials: String,
    val score: Int,
    val streakDays: Int,
    val completedThisWeek: Int,
)
