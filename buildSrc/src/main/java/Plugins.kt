import models.Plugin

object Plugins {

    val APPLICATION = Plugin("com.android.application", "7.2.2")
    val LIBRARY = Plugin("com.android.library", "7.2.2")
    val KOTLIN = Plugin("org.jetbrains.kotlin.android", "1.7.0")
    val SPOTLESS = Plugin("com.diffplug.spotless", "6.10.0")
    val MAVEN_PUBLISH = Plugin("com.vanniktech.maven.publish")
    val DOKKA = Plugin("org.jetbrains.dokka", "1.7.10")

    val CUSTOM_LIBRARY_MODULE = Plugin("library-module")
}