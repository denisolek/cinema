package com.denisolek.cinema.application

import com.denisolek.cinema.domain.authentication.Authentication.Companion.applicationAuth
import com.denisolek.cinema.domain.movie.MovieFacade
import com.denisolek.cinema.domain.shared.MovieId
import com.denisolek.cinema.infrastructure.ApplicationProperties
import mu.KotlinLogging.logger
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@Configuration
@EnableScheduling
class Scheduler(private val properties: ApplicationProperties, private val movieFacade: MovieFacade) {
    private val log = logger {}

    @EventListener
    fun onStartup(event: ContextRefreshedEvent) {
        if (properties.autoLoadMovies) loadMovies()
    }

    @Scheduled(cron = "0 0 * * * *")
    fun reloadMovies() {
        if (properties.autoLoadMovies) loadMovies()
    }

    private fun loadMovies() {
        log.info { "Loading ${properties.supportedMovies} movies" }
        properties.supportedMovies
            .map { MovieId(it) }
            .let { movieFacade.loadMovies(applicationAuth, it) }
    }
}