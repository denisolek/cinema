package com.denisolek.cinema.application.config

import com.denisolek.cinema.domain.movie.MovieFacade
import com.denisolek.cinema.domain.review.ReviewFacade
import com.denisolek.cinema.infrastructure.event.SpringDomainEventPublisher
import com.denisolek.cinema.infrastructure.persistance.InMemoryReviewRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ReviewConfiguration {

    @Bean
    fun reviewFacade(
        movieFacade: MovieFacade,
        springDomainEventPublisher: SpringDomainEventPublisher
    ) = ReviewFacade(InMemoryReviewRepository(), movieFacade, springDomainEventPublisher)
}