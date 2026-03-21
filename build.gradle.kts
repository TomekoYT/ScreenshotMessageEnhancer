val mod_name = property("mod_name")
val mod_id = property("mod_id")
val mod_version = property("mod_version")
val mod_description = property("mod_description")
val mod_archive_name = property("mod_archive_name")

val minecraft_version = property("minecraft_version")
val yarn_mappings_version = property("yarn_mappings_version")
val fabric_loader_version = property("fabric_loader_version")
val fabric_api_version = property("fabric_api_version")

val yacl_version = property("yacl_version")
val mod_menu_version = property("mod_menu_version")

plugins {
	id("net.fabricmc.fabric-loom-remap")
}

base {
	archivesName.set("$mod_archive_name" + "_$mod_version" + "_$minecraft_version+_fabric")
}

repositories {
	maven("https://maven.isxander.dev/releases")
	maven("https://maven.terraformersmc.com/")
}

dependencies {
	minecraft("com.mojang:minecraft:$minecraft_version")
	mappings("net.fabricmc:yarn:$yarn_mappings_version:v2")
	modImplementation("net.fabricmc:fabric-loader:$fabric_loader_version")
	modImplementation("net.fabricmc.fabric-api:fabric-api:$fabric_api_version")

	modImplementation("dev.isxander:yet-another-config-lib:$yacl_version")
	modImplementation("com.terraformersmc:modmenu:$mod_menu_version")
}

tasks.processResources {
	val props = mapOf(
		"mod_id" to mod_id,
		"mod_name" to mod_name,
		"mod_version" to mod_version,
		"mod_description" to mod_description,

		"minecraft_version" to minecraft_version,
		"fabric_loader_version" to fabric_loader_version,
		"fabric_api_version" to fabric_api_version,

		"yacl_version" to yacl_version,
		"mod_menu_version" to mod_menu_version
	)

	inputs.properties(props)

	filesMatching("fabric.mod.json") {
		expand(props)
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.release = 21
}

java {
	withSourcesJar()
	sourceCompatibility = JavaVersion.VERSION_21
	targetCompatibility = JavaVersion.VERSION_21
}

tasks.jar {
	inputs.property("archivesName", base.archivesName)

	from("LICENSE") {
		rename { "${it}_${base.archivesName.get()}" }
	}
}
