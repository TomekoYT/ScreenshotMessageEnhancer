pluginManagement {
	repositories {
		mavenCentral()
		gradlePluginPortal()

		maven("https://maven.fabricmc.net")
		maven("https://jitpack.io/")
		maven("https://maven.architectury.dev")
		maven("https://maven.kikugie.dev/snapshots")
		maven("https://maven.kikugie.dev/releases")
	}
}

plugins {
	id("dev.kikugie.stonecutter") version providers.gradleProperty("stonecutter_version")
}

stonecutter {
	create(rootProject) {
		versions("1.21.10", "1.21.11")
		version("26.1").buildscript("unobfuscated.gradle.kts")
		vcsVersion = providers.gradleProperty("stonecutter_minecraft_version")
	}
}

dependencyResolutionManagement {
	versionCatalogs {
		create("libs")
	}
}

rootProject.name = "ScreenshotMessageEnhancer"