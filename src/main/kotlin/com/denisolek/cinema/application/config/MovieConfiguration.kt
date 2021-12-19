package com.denisolek.cinema.application.config

import com.denisolek.cinema.domain.movie.MovieFacade
import com.denisolek.cinema.infrastructure.event.SpringDomainEventPublisher
import com.denisolek.cinema.infrastructure.http.omdb.OMDbRepository
import com.denisolek.cinema.infrastructure.persistance.InMemoryMovieRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MovieConfiguration {

    @Bean
    fun movieFacade(
        omDbRepository: OMDbRepository,
        springDomainEventPublisher: SpringDomainEventPublisher
    ) = MovieFacade(InMemoryMovieRepository(), omDbRepository, springDomainEventPublisher)
}