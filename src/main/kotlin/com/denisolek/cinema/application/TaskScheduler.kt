package com.denisolek.cinema.application

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
        log.info { "Loading ${properties.availableMovies} movies on application startup" }
        properties.availableMovies
            .map { MovieId(it) }
            .let { movieFacade.loadMovies(it) }
    }
}