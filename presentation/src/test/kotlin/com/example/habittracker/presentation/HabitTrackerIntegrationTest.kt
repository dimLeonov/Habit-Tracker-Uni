package com.example.habittracker.presentation

import com.example.habittracker.application.HabitCalendarService
import com.example.habittracker.application.HabitCreateCommand
import com.example.habittracker.application.HabitStatusView
import com.example.habittracker.application.HabitTrackerService
import com.example.habittracker.domain.HabitFrequency
import com.example.habittracker.domain.HabitVisibility
import com.example.habittracker.presentation.web.config.API_VERSION_1
import com.example.habittracker.presentation.web.config.API_VERSION_2
import com.example.habittracker.presentation.web.config.API_VERSION_HEADER
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.client.ApiVersionInserter
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
class HabitTrackerIntegrationTest {
    @Autowired
    private lateinit var habitTracker: HabitTrackerService

    @Autowired
    private lateinit var habitCalendar: HabitCalendarService

    @Autowired
    private lateinit var context: WebApplicationContext

    @Test
    fun `seeded public dashboard contains leaderboard data`() {
        val dashboard = habitTracker.publicDashboard()

        assertTrue(dashboard.stats.any { it.label == "Published habits" })
        assertEquals("marta", dashboard.leaderboard.first().name)
    }

    @Test
    fun `registration creates default workspace dashboard`() {
        habitTracker.register("newuser", "password")

        val dashboard = habitTracker.dashboardFor("newuser")

        assertEquals("newuser", dashboard.userName)
        assertEquals("0/0", dashboard.stats.first { it.label == "Completed today" }.value)
    }

    @Test
    fun `calendar shows persisted completion rhythm`() {
        val calendar = habitCalendar.calendarFor("marta")

        assertEquals("marta", calendar.userName)
        assertTrue(calendar.weeks.isNotEmpty())
        assertTrue(calendar.completedDays > 0)
    }

    @Test
    fun `simple habit can be created and completed`() {
        habitTracker.register("tracker", "password")
        habitTracker.createHabit(
            "tracker",
            HabitCreateCommand(
                name = "Drink water",
                category = "Health",
                frequency = HabitFrequency.DAILY,
                goal = "8 glasses",
                visibility = HabitVisibility.PRIVATE,
                steps = emptyList(),
            ),
        )
        val habit = habitTracker.dashboardFor("tracker").habits.single()

        habitTracker.markHabitDone("tracker", habit.id)

        val dashboard = habitTracker.dashboardFor("tracker")
        assertEquals(HabitStatusView.DONE, dashboard.habits.single().status)
        assertEquals("1/1", dashboard.stats.first { it.label == "Completed today" }.value)
    }

    @Test
    fun `versioned leaderboard api returns compact v1 response`() {
        restTestClient()
            .get()
            .uri("/api/leaderboard")
            .apiVersion(API_VERSION_1)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
            .jsonPath("$.entries[0].name")
            .isEqualTo("marta")
            .jsonPath("$.entries[0].initials")
            .doesNotExist()
    }

    @Test
    fun `versioned leaderboard api returns richer v2 response by default`() {
        restTestClient()
            .get()
            .uri("/api/leaderboard")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
            .jsonPath("$.generatedAt")
            .exists()
            .jsonPath("$.entries[0].name")
            .isEqualTo("marta")
            .jsonPath("$.entries[0].initials")
            .isEqualTo("M")
    }

    private fun restTestClient(): RestTestClient = RestTestClient
        .bindTo(mockMvc())
        .apiVersionInserter(ApiVersionInserter.useHeader(API_VERSION_HEADER))
        .defaultApiVersion(API_VERSION_2)
        .build()

    private fun mockMvc(): MockMvc = MockMvcBuilders
        .webAppContextSetup(context)
        .apply<org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder>(springSecurity())
        .build()
}
