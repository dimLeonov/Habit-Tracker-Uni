plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-allopen:${libs.versions.kotlin.get()}")
    implementation("dev.detekt:detekt-gradle-plugin:${libs.versions.detekt.get()}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:${libs.versions.spring.boot.get()}")
}

gradlePlugin {
    plugins {
        register("kotlinModule") {
            id = "habittracker.kotlin-module"
            implementationClass = "com.example.habittracker.build.HabitTrackerKotlinModulePlugin"
        }
        register("springBootApp") {
            id = "habittracker.spring-boot-app"
            implementationClass = "com.example.habittracker.build.HabitTrackerSpringBootAppPlugin"
        }
    }
}
