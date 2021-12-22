package com.denisolek.cinema.infrastructure

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("setup")
class ApplicationProperties(
    val autoLoadMovies: Boolean = true,
    val useMongo: Boolean = false,
    val supportedMovies: List<String>
)