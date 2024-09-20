import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("app.cash.sqldelight") version "2.0.2"
    kotlin("plugin.serialization") version "2.0.20"
}

group = "com.chenxinzhi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation("app.cash.sqldelight:sqlite-driver:2.0.2")
    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:2.0.20")

    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation("androidx.graphics:graphics-shapes-desktop:1.0.1")
    val fxSuffix = when (osdetector.classifier) {
        "linux-x86_64" -> "linux"
        "linux-aarch_64" -> "linux-aarch64"
        "windows-x86_64" -> "win"
        "osx-x86_64" -> "mac"
        "osx-aarch_64" -> "mac-aarch64"
        else -> throw IllegalStateException("Unknown OS: ${osdetector.classifier}")
    }
    implementation("org.openjfx:javafx-base:21.0.4:${fxSuffix}")
    implementation("org.openjfx:javafx-graphics:21.0.4:${fxSuffix}")
    implementation("org.openjfx:javafx-controls:21.0.4:${fxSuffix}")
    implementation("org.openjfx:javafx-swing:21.0.4:${fxSuffix}")
    implementation("org.openjfx:javafx-web:21.0.4:${fxSuffix}")
    implementation("org.openjfx:javafx-media:21.0.4:${fxSuffix}")
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.9.0-RC.2")
    implementation(compose.desktop.currentOs)
    api("moe.tlaster:precompose:1.6.2")
    api("moe.tlaster:precompose-viewmodel:1.6.2")
    val ktor_version = "2.3.12"
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")


}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
            packageName = "deskApp"
            packageVersion = "1.0.0"
        }
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("com.chenxinzhi.repository")
        }
    }
}
