dependencies {
    // Module dependencies - depends on all modules in the repository
    testImplementation(projects.modules.springboardCore)
    testImplementation(projects.modules.springboardExpression)
    testImplementation(projects.modules.springboardInference)

    // Standard library
    testImplementation(libs.kotlin.stdlib)

    // Test dependencies - using specification bundle
    testImplementation(libs.bundles.specification)
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
