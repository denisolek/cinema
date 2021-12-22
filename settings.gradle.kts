rootProject.name = "cinema"

pluginManagement {
    val springBootVersion: String by settings
    val kotlinVersion: String by settings
    val testLoggerVersion: String by settings
    plugins {
        id("org.springframework.boot") version springBootVersion
        id("com.adarshr.test-logger") version testLoggerVersion
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
    }
}