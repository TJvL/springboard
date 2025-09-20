plugins { alias(libs.plugins.antlr) }

description = "Springboard Expression Module - ANTLR-based expression parsing and evaluation"

dependencies {
    implementation(libs.kotlin.stdlib)

    implementation(libs.antlr.runtime)
    antlr(libs.antlr.tool)

    implementation(libs.javamoney.moneta)

    api(projects.modules.springboardCore)

    testImplementation(libs.bundles.test)
}

sourceSets { main { java { srcDir("${layout.buildDirectory.get()}/generated-src/antlr/main") } } }

tasks.generateGrammarSource {
    maxHeapSize = "64m"
    arguments = arguments + listOf("-visitor", "-long-messages")
    outputDirectory = file("${layout.buildDirectory.get()}/generated-src/antlr/main")
}

tasks.compileKotlin { dependsOn(tasks.generateGrammarSource) }

tasks.compileTestKotlin { dependsOn(tasks.generateTestGrammarSource) }

// Fix Spotless task dependencies to run after ANTLR generation
tasks.named("spotlessCheck") { dependsOn(tasks.generateGrammarSource) }

tasks.named("spotlessApply") { dependsOn(tasks.generateGrammarSource) }
