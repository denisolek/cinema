package com.denisolek.cinema.application.config

import com.denisolek.cinema.domain.movie.MovieFacade
import com.denisolek.cinema.domain.show.ShowFacade
import com.denisolek.cinema.domain.show.infrastructure.ShowRepository
import com.denisolek.cinema.infrastructure.event.SpringDomainEventPublisher
import com.denisolek.cinema.infrastructure.persistance.InMemoryShowRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class ShowConfiguration {

    @Bean
    fun showRepository() = InMemoryShowRepository()

    @Bean
    fun showFacade(
        showRepository: ShowRepository,
        movieFacade: MovieFacade,
        springDomainEventPublisher: SpringDomainEventPublisher
    ) = ShowFacade(showRepository, movieFacade, springDomainEventPublisher)
}