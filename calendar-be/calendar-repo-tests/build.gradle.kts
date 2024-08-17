plugins {
    id("build-kmp")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(kotlin("test-common"))
                api(kotlin("test-annotations-common"))

                api(libs.coroutines.core)
                api(libs.coroutines.test)
                implementation(projects.calendarCommon)
                implementation(projects.calendarRepoCommon)
            }
        }
        commonTest {
            dependencies {
                implementation(projects.calendarStubs)
            }
        }
        jvmMain {
            dependencies {
                api(kotlin("test-junit"))
            }
        }
    }
}
