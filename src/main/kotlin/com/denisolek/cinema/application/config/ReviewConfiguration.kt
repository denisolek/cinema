package com.denisolek.cinema.application.config

import com.denisolek.cinema.domain.movie.MovieFacade
import com.denisolek.cinema.domain.review.ReviewEventHandler
import com.denisolek.cinema.domain.review.ReviewFacade
import com.denisolek.cinema.domain.review.infrastructure.ReviewRepository
import com.denisolek.cinema.infrastructure.event.SpringDomainEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ReviewConfiguration {

    @Bean
    fun reviewFacade(
        reviewRepository: ReviewRepository,
        movieFacade: MovieFacade,
        springDomainEventPublisher: SpringDomainEventPublisher
    ) = ReviewFacade(reviewRepository, movieFacade, springDomainEventPublisher)

    @Bean
    fun reviewEventHandler(
        reviewRepository: ReviewRepository,
        springDomainEventPublisher: SpringDomainEventPublisher
    ) = ReviewEventHandler(reviewRepository, springDomainEventPublisher)
}