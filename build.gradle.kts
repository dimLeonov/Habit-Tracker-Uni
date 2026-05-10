plugins {
    id("habittracker.kotlin-module") apply false
    id("habittracker.spring-boot-app") apply false
}

subprojects {
    apply(plugin = "habittracker.kotlin-module")
}

tasks.register("detektFormat") {
    description = "Runs detekt auto-correction for all modules."
    group = "formatting"
    dependsOn(subprojects.map { it.tasks.named("detektFormat") })
}
