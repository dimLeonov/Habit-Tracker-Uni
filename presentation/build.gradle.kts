plugins {
    id("habittracker.spring-boot-app")
}

dependencies {
    implementation(project(":application"))
    implementation(libs.jackson.module.kotlin)
    implementation(libs.kotlin.css)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.html)
    implementation(libs.spring.boot.starter.web)

    testImplementation(libs.spring.boot.starter.test)
}

springBoot {
    mainClass.set("com.example.habittracker.presentation.HabitTrackerApplicationKt")
}
