package com.denisolek.cinema.application

import com.denisolek.cinema.domain.authentication.Authentication.Companion.applicationAuth
import com.denisolek.cinema.domain.movie.MovieFacade
import com.denisolek.cinema.domain.shared.MovieId
import com.denisolek.cinema.infrastructure.ApplicationProperties
import mu.KotlinLogging.logger
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class TaskScheduler(private val properties: ApplicationProperties, val movieFacade: MovieFacade) {
    private val log = logger {}

    @EventListener
    fun onStartup(event: ContextRefreshedEvent) {
        if (properties.loadMoviesOnStartup) loadMovies()
    }

    private fun loadMovies() {
        log.info { "Loading ${properties.availableMovies} movies on application startup" }
        properties.availableMovies
            .map { MovieId(it) }
            .let { movieFacade.loadMovies(applicationAuth, it) }
    }
}