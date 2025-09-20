plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.spring) apply false
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependency.management) apply false
    alias(libs.plugins.graalvm.native) apply false
    alias(libs.plugins.spotless)
}

group = "dev.tjvl.springboard"
version = "1.0-SNAPSHOT"

repositories { mavenCentral() }

extra["springModulithVersion"] =
    libs.versions.spring.modulith
        .get()

spotless {
    kotlin {
        target("**/*.kt")
        ktlint(libs.versions.ktlint.get())
        trimTrailingWhitespace()
        endWithNewline()
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        ktlint(libs.versions.ktlint.get())
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
        plugin =
            rootProject.libs.plugins.kotlin.jvm
                .get()
                .pluginId,
    )
    apply(
        plugin =
            rootProject.libs.plugins.spotless
                .get()
                .pluginId,
    )

    configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(
                JavaLanguageVersion.of(
                    rootProject.libs.versions.java
                        .get(),
                ),
            )
        }
    }

    repositories { mavenCentral() }

    // Configure dependency management for Spring Boot modules
    pluginManager.withPlugin("io.spring.dependency-management") {
        configure<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension> {
            imports {
                mavenBom(
                    rootProject.libs.spring.boot.dependencies
                        .get()
                        .toString(),
                )
                mavenBom(
                    rootProject.libs.spring.modulith.bom
                        .get()
                        .toString(),
                )
            }
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging { events("passed", "skipped", "failed") }
    }

    tasks.named("check") { dependsOn("spotlessCheck") }

    tasks.named("build") { dependsOn("check") }

    // Configure Kotlin compiler options for Spring Boot compatibility
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions { freeCompilerArgs.addAll("-Xjsr305=strict") }
    }
}

tasks.register("applyFormat") {
    group = "verification"
    description = "Fix code formatting"
    dependsOn("spotlessApply")
}
