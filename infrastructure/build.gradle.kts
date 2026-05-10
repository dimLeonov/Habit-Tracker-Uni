plugins {
    alias(libs.plugins.jpa)
}

dependencies {
    implementation(project(":application"))
    implementation(project(":domain"))
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.security)
    runtimeOnly(libs.h2)
}
