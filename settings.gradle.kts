rootProject.name = "cinema"

pluginManagement {
    val springBootVersion: String by settings
    val kotlinVersion: String by settings
    plugins {
        id("org.springframework.boot") version springBootVersion
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
    }
}