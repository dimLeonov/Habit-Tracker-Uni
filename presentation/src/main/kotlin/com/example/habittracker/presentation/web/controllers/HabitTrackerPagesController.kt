package com.example.habittracker.presentation.web.controllers

import com.example.habittracker.application.HabitCalendarService
import com.example.habittracker.application.HabitCreateCommand
import com.example.habittracker.application.HabitTrackerService
import com.example.habittracker.application.HabitUpdateCommand
import com.example.habittracker.domain.HabitFrequency
import com.example.habittracker.domain.HabitVisibility
import com.example.habittracker.presentation.web.i18n.UiMessages
import com.example.habittracker.presentation.web.pages.CsrfView
import com.example.habittracker.presentation.web.pages.calendarPage
import com.example.habittracker.presentation.web.pages.habitPlacePage
import com.example.habittracker.presentation.web.pages.homePage
import com.example.habittracker.presentation.web.pages.leaderboardPage
import com.example.habittracker.presentation.web.pages.loginPage
import com.example.habittracker.presentation.web.pages.registerPage
import com.example.habittracker.presentation.web.pages.workspacePage
import org.springframework.context.MessageSource
import org.springframework.http.MediaType
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import java.security.Principal
import java.util.Locale

@Suppress("TooManyFunctions")
@Controller
class HabitTrackerPagesController(
    private val habitTracker: HabitTrackerService,
    private val habitCalendar: HabitCalendarService,
    private val messageSource: MessageSource,
) {
    @GetMapping("/", produces = [MediaType.TEXT_HTML_VALUE])
    @ResponseBody
    fun home(
        principal: Principal?,
        csrfToken: CsrfToken,
        locale: Locale,
    ): String = homePage(
        principal?.name?.let { habitTracker.dashboardFor(it) } ?: habitTracker.publicDashboard(),
        messages(locale),
        principal != null,
        csrfToken.toView(),
    )

    @GetMapping("/login", produces = [MediaType.TEXT_HTML_VALUE])
    @ResponseBody
    fun login(
        csrfToken: CsrfToken,
        @RequestParam(required = false) error: String?,
        locale: Locale,
    ): String = loginPage(csrfToken.toView(), error != null, messages(locale))

    @GetMapping("/register", produces = [MediaType.TEXT_HTML_VALUE])
    @ResponseBody
    fun register(
        csrfToken: CsrfToken,
        @RequestParam(required = false) error: String?,
        locale: Locale,
    ): String =
        registerPage(csrfToken.toView(), error?.let { messages(locale).text("auth.register.error") }, messages(locale))

    @PostMapping("/register")
    fun createAccount(@RequestParam username: String, @RequestParam password: String): String = runCatching {
        habitTracker.register(username, password)
    }.fold(
        onSuccess = { "redirect:/login" },
        onFailure = { "redirect:/register?error" },
    )

    @GetMapping("/habits", produces = [MediaType.TEXT_HTML_VALUE])
    @ResponseBody
    fun habitPlace(
        principal: Principal,
        csrfToken: CsrfToken,
        locale: Locale,
    ): String = habitPlacePage(habitTracker.dashboardFor(principal.name), csrfToken.toView(), messages(locale))

    @PostMapping("/habits")
    fun createHabit(
        principal: Principal,
        @RequestParam name: String,
        @RequestParam category: String,
        @RequestParam frequency: HabitFrequency,
        @RequestParam goal: String,
        @RequestParam visibility: HabitVisibility,
        @RequestParam(required = false, defaultValue = "") steps: String,
    ): String {
        habitTracker.createHabit(
            principal.name,
            HabitCreateCommand(
                name = name,
                category = category,
                frequency = frequency,
                goal = goal,
                visibility = visibility,
                steps = steps.lines(),
            ),
        )
        return "redirect:/workspace"
    }

    @PostMapping("/habits/suggested")
    fun addSuggestedHabit(principal: Principal, @RequestParam suggestionId: Long): String {
        habitTracker.addSuggestedHabit(principal.name, suggestionId)
        return "redirect:/workspace"
    }

    @GetMapping("/workspace", produces = [MediaType.TEXT_HTML_VALUE])
    @ResponseBody
    fun workspace(
        principal: Principal,
        csrfToken: CsrfToken,
        locale: Locale,
    ): String = workspacePage(habitTracker.dashboardFor(principal.name), csrfToken.toView(), messages(locale))

    @GetMapping("/calendar", produces = [MediaType.TEXT_HTML_VALUE])
    @ResponseBody
    fun calendar(
        principal: Principal,
        csrfToken: CsrfToken,
        @RequestParam(required = false) month: String?,
        locale: Locale,
    ): String =
        calendarPage(habitCalendar.calendarFor(principal.name, month), messages(locale), locale, csrfToken.toView())

    @PostMapping("/workspace/habits/{habitId}")
    fun updateHabit(
        principal: Principal,
        @PathVariable habitId: Long,
        @RequestParam name: String,
        @RequestParam category: String,
        @RequestParam frequency: HabitFrequency,
        @RequestParam goal: String,
        @RequestParam visibility: HabitVisibility,
        @RequestParam(required = false, defaultValue = "") steps: String,
    ): String {
        habitTracker.updateHabit(
            principal.name,
            habitId,
            HabitUpdateCommand(
                name = name,
                category = category,
                frequency = frequency,
                goal = goal,
                visibility = visibility,
                steps = steps.lines(),
            ),
        )
        return "redirect:/workspace"
    }

    @PostMapping("/workspace/habits/{habitId}/done")
    fun markHabitDone(principal: Principal, @PathVariable habitId: Long): String {
        habitTracker.markHabitDone(principal.name, habitId)
        return "redirect:/workspace"
    }

    @PostMapping("/workspace/habits/{habitId}/steps/{stepId}")
    fun setStepCompletion(
        principal: Principal,
        @PathVariable habitId: Long,
        @PathVariable stepId: Long,
        @RequestParam completed: Boolean,
    ): String {
        habitTracker.setStepCompletion(principal.name, habitId, stepId, completed)
        return "redirect:/workspace"
    }

    @PostMapping("/workspace/habits/{habitId}/visibility")
    fun toggleVisibility(principal: Principal, @PathVariable habitId: Long): String {
        habitTracker.toggleHabitVisibility(principal.name, habitId)
        return "redirect:/workspace"
    }

    @PostMapping("/workspace/habits/{habitId}/delete")
    fun deleteHabit(principal: Principal, @PathVariable habitId: Long): String {
        habitTracker.deleteHabit(principal.name, habitId)
        return "redirect:/workspace"
    }

    @GetMapping("/leaderboard", produces = [MediaType.TEXT_HTML_VALUE])
    @ResponseBody
    fun leaderboard(
        principal: Principal?,
        csrfToken: CsrfToken,
        locale: Locale,
    ): String = leaderboardPage(
        principal?.name?.let { habitTracker.dashboardFor(it) } ?: habitTracker.publicDashboard(),
        messages(locale),
        principal != null,
        csrfToken.toView(),
    )

    private fun messages(locale: Locale): UiMessages = UiMessages(messageSource, locale)
}

private fun CsrfToken.toView(): CsrfView = CsrfView(parameterName, token)
