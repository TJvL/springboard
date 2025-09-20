plugins {
    alias(libs.plugins.spring.dependency.management)
}

description = "Springboard Inference Module - Logic inference and reasoning capabilities"

dependencies {
    api(projects.modules.springboardCore)
    api(projects.modules.springboardExpression)

    testImplementation(libs.bundles.unit.test)
}
