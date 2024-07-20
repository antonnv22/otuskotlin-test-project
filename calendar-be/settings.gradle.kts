rootProject.name = "calendar-be"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("../build-plugin")
    plugins {
        id("build-jvm") apply false
        id("build-kmp") apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":calendar-api-v1-jackson")
include(":calendar-api-v1-mappers")
include(":calendar-api-v2-kmp")
include(":calendar-api-log1")

include(":calendar-common")
include(":calendar-app-common")
include(":calendar-stubs")

include(":calendar-biz")

//include(":calendar-tmp")
