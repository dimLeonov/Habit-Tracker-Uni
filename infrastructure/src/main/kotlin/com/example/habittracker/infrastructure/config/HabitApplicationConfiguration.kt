package com.example.habittracker.infrastructure.config

import com.example.habittracker.application.CompositeHabitEventPublisher
import com.example.habittracker.application.HabitDraftFactory
import com.example.habittracker.application.HabitEventListener
import com.example.habittracker.application.HabitEventPublisher
import com.example.habittracker.application.HabitProgressReporter
import com.example.habittracker.application.LeaderboardRanker
import com.example.habittracker.domain.CompletionPolicyFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class HabitApplicationConfiguration {
    @Bean
    open fun completionPolicyFactory(): CompletionPolicyFactory = CompletionPolicyFactory()

    @Bean
    open fun habitDraftFactory(): HabitDraftFactory = HabitDraftFactory()

    @Bean
    open fun habitProgressReporter(completionPolicyFactory: CompletionPolicyFactory): HabitProgressReporter =
        HabitProgressReporter(completionPolicyFactory)

    @Bean
    open fun leaderboardRanker(): LeaderboardRanker = LeaderboardRanker()

    @Bean
    open fun habitEventPublisher(listeners: List<HabitEventListener>): HabitEventPublisher =
        CompositeHabitEventPublisher(listeners)
}
