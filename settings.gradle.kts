pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://repo.spring.io/milestone")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven("https://repo.spring.io/milestone")
    }
}

rootProject.name = "habit-tracker-uni"

include(
    "application",
    "domain",
    "infrastructure",
    "presentation",
)
