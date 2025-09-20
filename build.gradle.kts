plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.spotless)
}

group = "dev.tjvl.springboard"

version = "1.0-SNAPSHOT"

repositories { mavenCentral() }

spotless {
    kotlin {
        target("**/*.kt")
        ktfmt(libs.versions.ktfmt.get()).kotlinlangStyle()
        trimTrailingWhitespace()
        endWithNewline()
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        ktfmt(libs.versions.ktfmt.get()).kotlinlangStyle()
        trimTrailingWhitespace()
        endWithNewline()
    }
    format("antlr") {
        target("**/*.g4")
        trimTrailingWhitespace()
        endWithNewline()
    }
    json {
        target("**/*.json")
        trimTrailingWhitespace()
        endWithNewline()
    }
    format("markdown") {
        target("**/*.md")
        trimTrailingWhitespace()
        endWithNewline()
    }
    format("toml") {
        target("**/*.toml")
        trimTrailingWhitespace()
        endWithNewline()
    }
    format("yaml") {
        target("**/*.yml", "**/*.yaml")
        trimTrailingWhitespace()
        endWithNewline()
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version

    apply(
        plugin = rootProject.libs.plugins.kotlin.jvm.get().pluginId,
    )
    apply(
        plugin = rootProject.libs.plugins.spotless.get().pluginId,
    )

    apply(
        plugin = rootProject.libs.plugins.detekt.get().pluginId,
    )

    configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
        toolVersion = rootProject.libs.versions.detekt.get()
        buildUponDefaultConfig = true
        allRules = true
        source.setFrom("src/main/kotlin", "src/test/kotlin")
        parallel = true
        ignoreFailures = false
        autoCorrect = true
        config.setFrom(files("${rootProject.projectDir}/detekt.yml"))
    }

    repositories { mavenCentral() }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging { events("passed", "skipped", "failed") }
    }

    tasks.named("check") { dependsOn("spotlessCheck", "detekt") }

    tasks.named("build") { dependsOn("check") }
}

tasks.register("formatAndLint") {
    group = "verification"
    description = "Apply code formatting and run static analysis"
    dependsOn("spotlessApply", "detekt")
}

tasks.register("fix") {
    group = "verification"
    description = "Fix code formatting and apply auto-corrections"
    dependsOn("spotlessApply")
}
