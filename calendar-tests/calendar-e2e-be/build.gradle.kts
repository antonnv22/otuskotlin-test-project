plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("ru.otus.otuskotlin.calendar:calendar-api-v1-jackson")
    implementation("ru.otus.otuskotlin.calendar:calendar-api-v2-kmp")

    testImplementation(libs.logback)
    testImplementation(libs.kermit)

    testImplementation(libs.bundles.kotest)

//    implementation("com.rabbitmq:amqp-client:$rabbitVersion")
//    implementation("org.apache.kafka:kafka-clients:$kafkaVersion")

    testImplementation(libs.testcontainers.core)
    testImplementation(libs.coroutines.core)

    testImplementation(libs.ktor.client.core)
    testImplementation(libs.ktor.client.okhttp)
//    testImplementation("io.ktor:ktor-client-core:$ktorVersion")
//    testImplementation("io.ktor:ktor-client-okhttp:$ktorVersion")
//    testImplementation("io.ktor:ktor-client-okhttp-jvm:$ktorVersion")
}

var severity: String = "MINOR"

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()
//        dependsOn(gradle.includedBuild(":calendar-app-spring").task("dockerBuildImage"))
//        dependsOn(gradle.includedBuild(":calendar-be").task(":calendar-app-ktor:publishImageToLocalRegistry"))
//        dependsOn(gradle.includedBuild(":calendar-app-rabbit").task("dockerBuildImage"))
//        dependsOn(gradle.includedBuild(":calendar-app-kafka").task("dockerBuildImage"))
    }
}
