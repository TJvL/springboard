plugins {
    alias(libs.plugins.spring.dependency.management)
}

dependencies {
    // Module dependencies - depends on all modules in the repository
    testImplementation(projects.modules.springboardCore)
    testImplementation(projects.modules.springboardExpression)
    testImplementation(projects.modules.springboardInference)

    // Test dependencies - using specification bundle
    testImplementation(libs.bundles.specification.test)
}

tasks.withType<Test> {
    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = false
        showStackTraces = true
        showCauses = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}
