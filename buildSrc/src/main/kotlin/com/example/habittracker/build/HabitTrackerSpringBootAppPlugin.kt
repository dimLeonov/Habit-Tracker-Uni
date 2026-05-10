package com.example.habittracker.build

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.springframework.boot.gradle.dsl.SpringBootExtension

class HabitTrackerSpringBootAppPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.spring")
            pluginManager.apply("org.springframework.boot")

            extensions.configure<SpringBootExtension> {
                mainClass.set("com.example.habittracker.presentation.HabitTrackerApplicationKt")
            }
        }
    }
}
