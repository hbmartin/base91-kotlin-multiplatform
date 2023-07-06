plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }
    js(IR) {
        browser()
        nodejs()
    }
    listOf(
        iosArm64(),
        iosSimulatorArm64(),
        iosX64(),
        macosArm64(),
        macosX64(),
        linuxX64(),
    ).forEach {
        if (it.konanTarget.family.isAppleFamily) {
            it.binaries.framework {
                baseName = "base91-streaming"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.base91)
                implementation(libs.okio)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
