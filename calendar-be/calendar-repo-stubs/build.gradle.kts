plugins {
    id("build-kmp")
}

kotlin {
    sourceSets {
        val coroutinesVersion: String by project

        val commonMain by getting {
            dependencies {
                implementation(projects.calendarCommon)
                implementation(projects.calendarStubs)

                implementation(libs.coroutines.core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(projects.calendarRepoTests)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}
