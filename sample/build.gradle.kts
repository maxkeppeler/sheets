/*
 *  Copyright (C) 2022. Maximilian Keppeler (https://www.maxkeppeler.com)
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
plugins {
    id(Plugins.APPLICATION.id)
    id(Plugins.KOTLIN.id)
}

android {
    namespace = App.ID
    defaultConfig {
        applicationId = App.ID
        compileSdk = App.COMPILE_SDK
        minSdk = App.MIN_SDK
        targetSdk = App.TARGET_SDK
        versionCode = App.VERSION_CODE
        versionName = App.VERSION_NAME
        testInstrumentationRunner = App.TEST_INSTRUMENTATION_RUNNER
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes.add("META-INF/atomicfu.kotlin_module")
            excludes.add("META-INF/core.kotlin_module")
        }
    }
}

dependencies {

    // Modules

    Modules.values().forEach { module ->
        kotlin.runCatching { apis(project(module.path)) }
    }

    // Google libs

    implementations(Dependencies.Google.MATERIAL)

    // Kotlin libs

    implementations(Dependencies.Kotlin.KOTLIN_STD)

    // AndroidX libs

    implementations(
        Dependencies.AndroidX.CORE_KTX,
        Dependencies.AndroidX.LIFECYCLE_KTX,
        Dependencies.AndroidX.CONSTRAINT_LAYOUT,
        Dependencies.AndroidX.APP_COMPAT,
        Dependencies.AndroidX.RECYCLER_VIEW,
    )
}