package com.denisolek.cinema

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
@SecurityScheme(name = "bearerAuth", type = HTTP, scheme = "bearer")
@OpenAPIDefinition(info = Info(title = "Cinema", version = "v1"))
class CinemaApplication

fun main(args: Array<String>) {
    runApplication<CinemaApplication>(*args)
}
