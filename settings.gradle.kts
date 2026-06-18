pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()

        maven("https://maven.fabricmc.net")
        maven("https://jitpack.io/")
        maven("https://maven.architectury.dev")
        maven("https://maven.kikugie.dev/snapshots")
        maven("https://maven.kikugie.dev/releases")

        maven("https://maven.deftu.dev/releases")
        maven("https://maven.deftu.dev/snapshots")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version providers.gradleProperty("stonecutter_version")
}

stonecutter {
    create(rootProject) {
        versions("26.1", "26.2")
        vcsVersion = "26.2"
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs")
    }
}

rootProject.name = "ScreenshotMessageEnhancer"