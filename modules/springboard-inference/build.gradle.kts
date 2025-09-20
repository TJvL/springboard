description = "Springboard Inference Module - Logic inference and reasoning capabilities"

dependencies {
    implementation(libs.kotlin.stdlib)

    api(projects.modules.springboardCore)
    api(projects.modules.springboardExpression)

    testImplementation(libs.bundles.test)
}
