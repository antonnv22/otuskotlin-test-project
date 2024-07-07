plugins {
    application
    id("build-jvm")
    alias(libs.plugins.muschko.java)
}

application {
    mainClass.set("ru.otus.otuskotlin.calendar.app.kafka.MainKt")
}

docker {
    javaApplication {
        baseImage.set("openjdk:17.0.2-slim")
    }
}

dependencies {
    implementation(libs.kafka.client)
    implementation(libs.coroutines.core)
    implementation(libs.kotlinx.atomicfu)

    implementation("ru.otus.otuskotlin.calendar.libs:calendar-lib-logging-logback")

    implementation(project(":calendar-app-common"))

    // transport models
    implementation(project(":calendar-common"))
    implementation(project(":calendar-api-v1-jackson"))
    implementation(project(":calendar-api-v1-mappers"))
    implementation(project(":calendar-api-v2-kmp"))
    // logic
    implementation(project(":calendar-biz"))

    testImplementation(kotlin("test-junit"))
}
