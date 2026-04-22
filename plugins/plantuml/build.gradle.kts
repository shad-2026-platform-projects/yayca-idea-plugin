plugins {
    id("org.jetbrains.intellij.platform")
    id("org.jetbrains.kotlin.jvm")
}

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    intellijPlatform {
        create("IC", "2024.3")
    }

    implementation(platform("com.fasterxml.jackson:jackson-bom:2.19.+"))
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("net.sourceforge.plantuml:plantuml:1.2026.+")
}

intellijPlatform {
    pluginConfiguration {
        name = "YAML Split Editor"
        version = project.version.toString()

        ideaVersion {
            sinceBuild = "243"
        }
    }
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "21"
        }
    }

    runIde {
        // IDE run configuration (optional tweaks)
    }
}