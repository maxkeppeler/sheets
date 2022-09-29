import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

gradlePlugin {
    plugins {
        register("library-module") {
            id = "library-module"
            implementationClass = "LibraryModulePlugin"
        }
    }
}