/*
 *  Copyright (C) 2020. Maximilian Keppeler (https://www.maxkeppeler.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
import com.android.build.gradle.LibraryExtension

plugins {
    id(Plugins.APPLICATION.id) version (Plugins.APPLICATION.version) apply false
    id(Plugins.LIBRARY.id) version (Plugins.APPLICATION.version) apply false
    id(Plugins.KOTLIN.id) version (Plugins.KOTLIN.version) apply false
    id(Plugins.SPOTLESS.id) version (Plugins.SPOTLESS.version)
    id(Plugins.DOKKA.id) version (Plugins.DOKKA.version)
}

buildscript {

    repositories {
        google()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }

    dependencies {
        classpath(Dependencies.Kotlin.GRADLE_PLUGIN)
        classpath(Dependencies.Gradle.BUILD)
        classpath(Dependencies.MAVEN_PUBLISH)
        classpath(Dependencies.DOKKA)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

tasks.dokkaHtmlMultiModule.configure {
    outputDirectory.set(projectDir.resolve("docs/api"))
}

subprojects {
    plugins.apply(Plugins.SPOTLESS.id)
    project.plugins.applyBaseConfig(project)
    spotless {
        kotlin {
            target("**/*.kt")
            licenseHeaderFile(rootProject.file("copyright.kt"))
        }
        kotlinGradle {
            target("*.gradle.kts", "gradle/*.gradle.kts", "buildSrc/*.gradle.kts")
            licenseHeaderFile(
                rootProject.file("copyright.kt"),
                "import|tasks|apply|plugins|rootProject"
            )
        }
    }
}


/**
 * Apply base configurations to the subjects that include specific custom plugins.
 */
fun PluginContainer.applyBaseConfig(project: Project) {
    whenPluginAdded {
        when (this) {
            is LibraryModulePlugin -> {
                project.extensions
                    .getByType<LibraryExtension>()
                    .apply {
                        baseLibraryConfig()
                    }
            }
        }
    }
}

/**
 * Apply base library configurations to the subprojects that include the plugin [LibraryModulePlugin].
 */
fun com.android.build.gradle.BaseExtension.baseLibraryConfig() {

    compileSdkVersion(App.COMPILE_SDK)

    defaultConfig {
        minSdk = App.MIN_SDK
        targetSdk = App.TARGET_SDK
        testInstrumentationRunner = App.TEST_INSTRUMENTATION_RUNNER
    }

    compileOptions.apply {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }

    buildFeatures.viewBinding = true

    packagingOptions.resources.excludes += listOf(
        "META-INF/DEPENDENCIES.txt",
        "META-INF/LICENSE",
        "META-INF/LICENSE.txt",
        "META-INF/NOTICE",
        "META-INF/NOTICE.txt",
        "META-INF/AL2.0",
        "META-INF/LGPL2.1"
    )

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}