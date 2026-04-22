pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("org.jetbrains.intellij.platform") version "2.12.0"
        id("org.jetbrains.kotlin.jvm") version "1.9.24"
    }
}

rootProject.name = "yayca-idea-plugin"
include("plugins:plantuml")