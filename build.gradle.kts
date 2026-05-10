plugins {
    java
    id("com.gradleup.shadow") version "8.3.6"
}

group = "com.keepmegrowing"
version = "2.0"
description = "KeepMeGrowing"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://maven.enginehub.org/repo/")
}

val bundleAllDbDrivers =
    providers.gradleProperty("bundleAllDbDrivers").map { it.equals("true", ignoreCase = true) }.getOrElse(false)

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")
    compileOnly("org.eclipse.jdt:org.eclipse.jdt.annotation:2.2.800")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.9")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.bstats:bstats-bukkit:3.2.1")

    val mysqlDep = "com.mysql:mysql-connector-j:8.3.0"
    val mariaDep = "org.mariadb.jdbc:mariadb-java-client:3.3.3"
    val postgresDep = "org.postgresql:postgresql:42.7.2"
    val sqliteDep = "org.xerial:sqlite-jdbc:3.45.3.0"
    val mongoDep = "org.mongodb:mongodb-driver-legacy:4.11.1"

    if (bundleAllDbDrivers) {
        implementation(mysqlDep)
        implementation(mariaDep)
        implementation(postgresDep)
        implementation(sqliteDep)
        implementation(mongoDep)
    } else {
        compileOnly(mysqlDep)
        compileOnly(mariaDep)
        compileOnly(postgresDep)
        compileOnly(sqliteDep)
        compileOnly(mongoDep)
    }
}

tasks.jar {
    enabled = false
}

tasks.shadowJar {
    archiveBaseName.set(
        if (bundleAllDbDrivers) {
            "keepmegrowing-bundled"
        } else {
            "keepmegrowing"
        },
    )
    archiveClassifier.set("")
    archiveVersion.set(project.version.toString())
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    mergeServiceFiles()
    relocate("org.bstats", "${project.group}")
}

tasks.assemble {
    dependsOn(tasks.shadowJar)
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}
