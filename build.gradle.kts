import java.security.MessageDigest
import java.util.Base64

plugins {
    id("java-library")
    id("maven-publish")
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.compileJava { options.encoding = "UTF-8" }
tasks.javadoc { options.encoding = "UTF-8" }

repositories {
    mavenCentral()
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.varoplugin.de/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://maven.enginehub.org/repo/")
}

val internal: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

val runtimeDownload: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

fun DependencyHandler.modularInternal(dependencyNotation: Any, localFileName: String): Dependency? {
    val file = file("${rootDir}/libs/${localFileName}.jar")
    return if (file.exists())
        this.add("internal", files(file))
    else
        this.add("internal", dependencyNotation)
}

dependencies {
    modularInternal("de.varoplugin:CFW:1.0.0", "CFW")
    implementation("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
}

tasks.jar {
    if (project.hasProperty("destinationDir"))
        destinationDirectory.set(file(project.property("destinationDir").toString()))
    if (project.hasProperty("fileName"))
        archiveFileName.set(project.property("fileName").toString())
}

val mdSha512: MessageDigest = MessageDigest.getInstance("SHA-512")
fun File.sha512(): ByteArray = mdSha512.digest(this.readBytes())
fun ByteArray.base64(): String = Base64.getEncoder().encodeToString(this)
