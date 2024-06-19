plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":calendar-api-v1-jackson"))
    implementation(project(":calendar-common"))

    testImplementation(kotlin("test-junit"))
}
