rootProject.name = "springboard"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    ":modules:springboard-core",
    ":modules:springboard-expression",
    ":modules:springboard-inference",
    ":modules:springboard-specification",
    ":modules:springboard-expression-web",
    ":modules:springboard-inference-web",
    ":modules:springboard-application",
)
