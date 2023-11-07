pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        val kotlinVersion: String by settings

        kotlin("jvm") version kotlinVersion
    }
 }

rootProject.name = "otuskotlin-test-project"

include("m1l1-first-app")