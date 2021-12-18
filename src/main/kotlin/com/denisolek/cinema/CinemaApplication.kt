package com.denisolek.cinema

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class CinemaApplication

fun main(args: Array<String>) {
    runApplication<CinemaApplication>(*args)
}
