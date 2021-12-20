package com.denisolek.cinema.application.config

import com.denisolek.cinema.infrastructure.persistance.InMemoryReviewRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ReviewRepositoryConfiguration {

    @Bean
    fun reviewRepository() = InMemoryReviewRepository()
}