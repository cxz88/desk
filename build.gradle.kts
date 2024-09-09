import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.openjfx.javafxplugin") version "0.0.13"
}

group = "com.chenxinzhi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation("io.github.khubaibkhan4:mediaplayer-kmp:1.0.9")
    val fxSuffix = when (osdetector.classifier) {
        "linux-x86_64" -> "linux"
        "linux-aarch_64" -> "linux-aarch64"
        "windows-x86_64" -> "win"
        "osx-x86_64" -> "mac"
        "osx-aarch_64" -> "mac-aarch64"
        else -> throw IllegalStateException("Unknown OS: ${osdetector.classifier}")
    }
    implementation("org.openjfx:javafx-base:19:${fxSuffix}")
    implementation("org.openjfx:javafx-graphics:19:${fxSuffix}")
    implementation("org.openjfx:javafx-controls:19:${fxSuffix}")
    implementation("org.openjfx:javafx-swing:19:${fxSuffix}")
    implementation("org.openjfx:javafx-web:19:${fxSuffix}")
    implementation("org.openjfx:javafx-media:19:${fxSuffix}")
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.9.0-RC.2")

    implementation(compose.desktop.currentOs)







}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb,TargetFormat.Exe)
            packageName = "deskApp"
            packageVersion = "1.0.0"
        }
    }
}
