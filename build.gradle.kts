plugins {
    kotlin("jvm") version "1.8.0"
    id("com.github.johnrengelman.shadow") version "7.1.1"
}

group = "kr.blugon"
version = "1.0.5"
val buildPath = File("C:/Files/Minecraft/Servers/\$plugins")


java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}


repositories {
    mavenCentral()
    mavenLocal()
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
}


tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
    }

    processResources {
        filesMatching("plugin.yml") {
            expand(project.properties)
        }
    }

    shadowJar {
        archiveVersion.set(project.version.toString())
        archiveBaseName.set(project.name)
        archiveFileName.set("${project.name}.jar")
        from(sourceSets["main"].output)

        doLast {
            copy {
                from(archiveFile)

                //Build Location
                into(buildPath)
            }
        }
    }

    jar {
        archiveVersion.set(project.version.toString())
        archiveBaseName.set(project.name)
        archiveFileName.set("${project.name}.jar")
        from(sourceSets["main"].output)

        doLast {
            copy {
                from(archiveFile)

                //Build Location
                into(buildPath)
            }
        }
    }
}