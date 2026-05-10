package com.example.habittracker.build

import dev.detekt.gradle.Detekt
import dev.detekt.gradle.extensions.DetektExtension
import dev.detekt.gradle.extensions.FailOnSeverity
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

class HabitTrackerKotlinModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = libs()
            val javaVersion = libs.version("java").toInt()
            val detektVersion = libs.version("detekt")
            val springBootDependencies = libs.library("spring-boot-dependencies")

            group = "com.example.habittracker"
            version = "0.1.0"

            pluginManager.apply("org.jetbrains.kotlin.jvm")
            pluginManager.apply("dev.detekt")

            dependencies {
                add("implementation", platform(springBootDependencies))
                add("testImplementation", platform(springBootDependencies))
                add("testImplementation", libs.library("kotlin-test"))
                add("detektPlugins", libs.library("detekt-rules-ktlint-wrapper"))
                add("detektPlugins", libs.library("detekt-rules-libraries"))
            }

            extensions.configure<KotlinJvmProjectExtension> {
                jvmToolchain(javaVersion)
            }

            extensions.configure<DetektExtension>("detekt") {
                toolVersion.set(detektVersion)
                config.setFrom(rootProject.file("config/detekt/detekt.yml"))
                buildUponDefaultConfig.set(true)
                allRules.set(true)
                parallel.set(true)
                ignoreFailures.set(false)
                failOnSeverity.set(FailOnSeverity.Warning)
                source.setFrom("src/main/kotlin", "src/test/kotlin")
                basePath.set(rootDir)
            }

            tasks.withType<Detekt>().configureEach {
                jvmTarget.set(javaVersion.toString())
                reports {
                    checkstyle.required.set(true)
                    html.required.set(true)
                    markdown.required.set(true)
                    sarif.required.set(true)
                }
            }

            tasks.register<Detekt>("detektFormat") {
                description = "Runs detekt with auto-correction enabled."
                group = "formatting"

                setSource(files("src/main/kotlin", "src/test/kotlin"))
                config.setFrom(rootProject.file("config/detekt/detekt.yml"))
                buildUponDefaultConfig.set(true)
                allRules.set(true)
                parallel.set(true)
                autoCorrect.set(true)
                ignoreFailures.set(false)
                failOnSeverity.set(FailOnSeverity.Warning)
                basePath.set(rootDir.absolutePath)
                include("**/*.kt", "**/*.kts")
                jvmTarget.set(javaVersion.toString())
            }

            tasks.matching { it.name == "check" }.configureEach {
                dependsOn("detekt")
            }

            tasks.withType<Test>().configureEach {
                useJUnitPlatform()
            }

            tasks.withType<KotlinCompilationTask<*>>().configureEach {
                compilerOptions {
                    freeCompilerArgs.add("-Xannotation-default-target=param-property")
                }
            }
        }
    }
}

private fun Project.libs(): VersionCatalog =
    rootProject.extensions.getByType<VersionCatalogsExtension>().named("libs")

private fun VersionCatalog.version(alias: String): String =
    findVersion(alias).orElseThrow {
        IllegalStateException("Version '$alias' is not defined in libs.versions.toml.")
    }.requiredVersion

private fun VersionCatalog.library(alias: String) =
    findLibrary(alias).orElseThrow {
        IllegalStateException("Library '$alias' is not defined in libs.versions.toml.")
    }
