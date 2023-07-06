import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import java.nio.charset.StandardCharsets

buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
    }
}
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("multiplatform") version libs.versions.kotlin.get() apply false
    id("io.gitlab.arturbosch.detekt") version libs.versions.detekt.get()
}

allprojects {
    group = project.property("GROUP") as String
    version = project.property("VERSION_NAME") as String

    repositories {
        mavenCentral()
        google()
    }
}

subprojects {
    apply<DetektPlugin>()
    tasks.withType<JavaCompile> {
        options.encoding = StandardCharsets.UTF_8.toString()
        sourceCompatibility = JavaVersion.VERSION_11.toString()
        targetCompatibility = JavaVersion.VERSION_11.toString()
    }
    dependencies {
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-rules-libraries:${rootProject.libs.versions.detekt.get()}")
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:${rootProject.libs.versions.detekt.get()}")
    }
    configure<DetektExtension> {
        config.setFrom(rootProject.file("detekt.yml"))
    }
}
