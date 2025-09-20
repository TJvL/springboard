plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.graalvm.native)
}

description = "Springboard Application - Main Spring Boot application that orchestrates all modules"

dependencies {
    // All web modules
    implementation(projects.modules.springboardInferenceWeb)
    implementation(projects.modules.springboardExpressionWeb)

    // Core modules (inherited through web modules, but explicit for clarity)
    implementation(projects.modules.springboardInference)
    implementation(projects.modules.springboardExpression)
    implementation(projects.modules.springboardCore)

    // Spring Boot starters (managed by BOM)
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

// GraalVM Native Image configuration
graalvmNative {
    binaries {
        named("main") {
            imageName.set("springboard-application")
            mainClass.set("dev.tjvl.springboard.application.SpringboardApplicationKt")
            // Create fully static native image with musl - no runtime dependencies
            // This allows us to use FROM scratch for maximum security and minimal size
            buildArgs.addAll("--static", "--libc=musl")
        }
    }
}

// Docker build tasks
tasks.register<Exec>("buildDockerImage") {
    group = "docker"
    description = "Build Docker image with GraalVM native compilation"
    
    val imageTag = "tjvl.dev/springboard:${project.version}"
    val latestTag = "tjvl.dev/springboard:latest"
    
    dependsOn("build")
    
    workingDir(rootProject.projectDir)
    
    commandLine(
        "docker", "build",
        "--file", "modules/springboard-application/Dockerfile",
        "--tag", imageTag,
        "--tag", latestTag,
        "."
    )
    
    doLast {
        println("✅ Native Docker image built successfully:")
        println("   📦 ${imageTag}")
        println("   📦 ${latestTag}")
    }
}
