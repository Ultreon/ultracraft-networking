import org.jetbrains.gradle.ext.Application
import org.jetbrains.gradle.ext.runConfigurations
import org.jetbrains.gradle.ext.settings
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date

//file:noinspection GroovyUnusedCatchParameter
buildscript {
    dependencies {
        classpath("gradle.plugin.org.jetbrains.gradle.plugin.idea-ext:gradle-idea-ext:1.1.7")
    }
}

plugins {
    id("idea")
    id("java")
    id("java-library")
    id("maven-publish")
}

apply(plugin = "org.jetbrains.gradle.plugin.idea-ext")

group = "io.github.ultreon.craftmods"
version = "0.1.0+snapshot.${DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm").format(Instant.now().atOffset(ZoneOffset.UTC))}"

println("Building version: $version")
println("Project name: $project.name")

sourceSets {
    create("api") {
        java {
            srcDir("src/api/java")
        }
    }

    main {
        java {
            compileClasspath += sourceSets["api"].output
            runtimeClasspath += sourceSets["api"].output
        }
    }
}

configurations {
    this["apiCompileClasspath"].extendsFrom(this["compileClasspath"])
}

repositories {
    mavenCentral()

    maven {
        name = "Ultracraft GitHub"
        url = uri("https://maven.pkg.github.com/Ultreon/ultracraft")

        credentials {
            username = (project.findProperty("gpr.user") ?: System.getenv("USERNAME")) as String
            password = (project.findProperty("gpr.key") ?: System.getenv("TOKEN")) as String
        }
    }

    maven {
        name = "Fabric"
        url = uri("https://maven.fabricmc.net/")
    }

    maven {
        name = "Jitpack"
        url = uri("https://jitpack.io")
    }
}

dependencies {
//    testImplementation(platform("org.junit:junit-bom:5.9.1"))
//    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("io.github.ultreon.craft:ultracraft-desktop:0.1.0+snapshot.2024.03.24.23.50")
}

fun setupIdea() {
    mkdir("$projectDir/build/gameutils")
    mkdir("$projectDir/run")
    mkdir("$projectDir/run/client")
    mkdir("$projectDir/run/client/alt")
    mkdir("$projectDir/run/client/main")
    mkdir("$projectDir/run/server")

    val ps = File.pathSeparator!!
    val files = configurations["runtimeClasspath"]!!.files
    files += tasks.compileJava.get().outputs.files.files

    val classPath = files.asSequence()
        .filter { it != null }
        .map { it.path }
        .joinToString(ps)

    //language=TEXT
    val conf = """
commonProperties
	fabric.development=true
    fabric.skipMcProvider=true
	fabric.log.disableAnsi=false
	log4j2.formatMsgNoLookups=true
    """.trimIndent()
    val launchFile = file("$projectDir/build/gameutils/launch.cfg")
    Files.writeString(
        launchFile.toPath(),
        conf,
        StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING,
        StandardOpenOption.WRITE
    )

    val cpFile = file("$projectDir/build/gameutils/classpath.txt")
    Files.writeString(
        cpFile.toPath(),
        classPath,
        StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING,
        StandardOpenOption.WRITE
    )

    idea {
        project {
            settings {
                withIDEADir {
                    println("Callback 1 executed with: $absolutePath")
                }

                runConfigurations {
                    create(
                        "Ultracraft Client",
                        Application::class.java
                    ) {                       // Create new run configuration "MyApp" that will run class foo.App
                        jvmArgs =
                            "-Xmx2g -Dfabric.dli.config=${launchFile.path} -Dfabric.dli.env=CLIENT -Dfabric.dli.main=net.fabricmc.loader.impl.launch.knot.KnotClient"
                        mainClass = "net.fabricmc.devlaunchinjector.Main"
                        moduleName = idea.module.name + ".main"
                        workingDirectory = "$projectDir/run/client/main/"
                        programParameters = "--gameDir=."
                    }
                    create(
                        "Ultracraft Client Alt",
                        Application::class.java
                    ) {                       // Create new run configuration "MyApp" that will run class foo.App
                        jvmArgs =
                            "-Xmx2g -Dfabric.dli.config=${launchFile.path} -Dfabric.dli.env=CLIENT -Dfabric.dli.main=net.fabricmc.loader.impl.launch.knot.KnotClient"
                        mainClass = "net.fabricmc.devlaunchinjector.Main"
                        moduleName = idea.module.name + ".main"
                        workingDirectory = "$projectDir/run/client/alt/"
                        programParameters = "--gameDir=."
                    }
                    create(
                        "Ultracraft Server",
                        Application::class.java
                    ) {                       // Create new run configuration "MyApp" that will run class foo.App
                        jvmArgs =
                            "-Xmx2g -Dfabric.dli.config=${launchFile.path} -Dfabric.dli.env=SERVER -Dfabric.dli.main=net.fabricmc.loader.impl.launch.knot.KnotClient"
                        mainClass = "net.fabricmc.devlaunchinjector.Main"
                        moduleName = idea.module.name + ".main"
                        workingDirectory = "$projectDir/run/server/"
                        programParameters = "--gameDir=."
                    }
                }
            }
        }
    }
    idea.module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

this.setupIdea()

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            groupId = "io.github.ultreon.craftmods"
            artifactId = "ultracraft-networking"
            version = project.version.toString()

            pom {
                name.set("Ultracraft Networking")
                description.set("Improved networking API for Ultracraft")

                url.set("https://github.com/Ultreon/ultracraft-networking")

                licenses {
                    license {
                        name.set("AGPL-3.0")
                        url.set("https://www.gnu.org/licenses/agpl-3.0.en.html")
                    }
                }

                developers {
                    developer {
                        id.set("XyperCode")
                        name.set("XyperCode")

                        url.set("https://github.com/XyperCode")
                    }
                }
            }
        }
    }
    
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Ultreon/ultracraft-networking")

            credentials {
                username = (project.findProperty("gpr.user") ?: System.getenv("USERNAME")) as String
                password = (project.findProperty("gpr.key") ?: System.getenv("TOKEN")) as String
            }
        }
    }
}
