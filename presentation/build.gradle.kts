plugins {
    id("habittracker.spring-boot-app")
}

dependencies {
    implementation(project(":application"))
    implementation(project(":domain"))
    implementation(project(":infrastructure"))
    implementation(libs.jackson.module.kotlin)
    implementation(libs.h2)
    implementation(libs.kotlin.css)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.html)
    implementation(libs.spring.boot.h2console)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.web)

    testImplementation(libs.spring.security.test)
    testImplementation(libs.spring.boot.starter.test)
}

springBoot {
    mainClass.set("com.example.habittracker.presentation.HabitTrackerApplicationKt")
}
