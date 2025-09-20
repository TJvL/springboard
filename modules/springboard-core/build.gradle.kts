plugins {
    alias(libs.plugins.spring.dependency.management)
}

description = "Springboard Core Module - Common utilities and base functionality"

dependencies {
    implementation(libs.javamoney.moneta)

    testImplementation(libs.bundles.unit.test)
}
