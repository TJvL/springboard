plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.dependency.management)
}

description = "Springboard Expression Web Module - Web API for expression parsing and evaluation"

dependencies {
    // Core modules
    implementation(projects.modules.springboardExpression)
    implementation(projects.modules.springboardCore)

    // Spring Boot reactive web stack
    implementation(libs.spring.boot.starter.webflux)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.data.redis.reactive)
    implementation(libs.spring.modulith.starter.core)

    // Kotlin reactive support
    implementation(libs.kotlinx.coroutines.reactor)

    // Testing
    testImplementation(libs.bundles.unit.test)
    testImplementation(libs.bundles.web.reactive.test)
}
