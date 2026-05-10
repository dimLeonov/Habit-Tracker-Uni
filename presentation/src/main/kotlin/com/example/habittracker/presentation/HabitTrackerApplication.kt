package com.example.habittracker.presentation

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.persistence.autoconfigure.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["com.example.habittracker"])
@EntityScan("com.example.habittracker.infrastructure.persistence")
@EnableJpaRepositories("com.example.habittracker.infrastructure.persistence")
class HabitTrackerApplication

fun main(args: Array<String>) {
    runApplication<HabitTrackerApplication>(*args)
}
