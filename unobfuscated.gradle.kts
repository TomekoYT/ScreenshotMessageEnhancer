val mod_name: String by project
val mod_id: String by project
val mod_version: String by project
val mod_description: String by project
val mod_archives_name: String by project

val java_version: String by project
val minecraft_version: String by project
val fabric_loader_version: String by project
val fabric_api_version: String by project

val yacl_version: String by project
val mod_menu_version: String by project

val java_objective_c_bridge_version: String by project

plugins {
    id("net.fabricmc.fabric-loom") version "1.15-SNAPSHOT"
}

base {
    archivesName.set("$mod_archives_name-$mod_version-$minecraft_version+_fabric")
}

repositories {
    maven("https://maven.isxander.dev/releases")
    maven("https://maven.terraformersmc.com/")
}

loom {
    runConfigs.all {
        ideConfigGenerated(stonecutter.current.isActive)
        runDir = "../../run"
    }
    runConfigs.remove(runConfigs["server"])
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraft_version")
    implementation("net.fabricmc:fabric-loader:$fabric_loader_version")
    implementation("net.fabricmc.fabric-api:fabric-api:$fabric_api_version")

    implementation("dev.isxander:yet-another-config-lib:$yacl_version")
    implementation("com.terraformersmc:modmenu:$mod_menu_version")

    implementation("ca.weblite:java-objc-bridge:${java_objective_c_bridge_version}")
}

tasks.processResources {
    val props = mapOf(
        "mod_id" to mod_id,
        "mod_name" to mod_name,
        "mod_version" to mod_version,
        "mod_description" to mod_description,

        "java_version" to java_version,
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


    val mixinProps = mapOf(
        "java_version" to java_version
    )

    inputs.properties(mixinProps)

    filesMatching("$mod_id.mixins.json") {
        expand(mixinProps)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release = java_version.toInt()
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.toVersion(java_version)
    targetCompatibility = JavaVersion.toVersion(java_version)
}

tasks.jar {
    inputs.property("archivesName", base.archivesName)

    from("LICENSE") {
        rename { "${it}_${base.archivesName.get()}" }
    }
}