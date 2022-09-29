pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
include(
    ":sample",
    // Include all modules
    ":core",
    ":info",
    ":storage",
    ":option",
    ":input",
    ":color",
    ":calendar",
    ":duration",
    ":clock",
    ":lottie",
)

