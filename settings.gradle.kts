rootProject.name = "springboard"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    ":modules:springboard-core",
    ":modules:springboard-expression",
    ":modules:springboard-inference",
    ":modules:springboard-specification",
)
