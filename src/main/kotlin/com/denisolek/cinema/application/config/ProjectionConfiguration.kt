package com.denisolek.cinema.application.config

import com.denisolek.cinema.domain.readmodel.MovieDetailsProjection
import com.denisolek.cinema.domain.readmodel.infrastructure.MovieDetailsRepository
import com.denisolek.cinema.infrastructure.persistance.InMemoryMovieDetailsRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ProjectionConfiguration {

    @Bean
    fun movieDetailsRepository() = InMemoryMovieDetailsRepository()

    @Bean
    fun movieDetailsProjection(
        movieDetailsRepository: MovieDetailsRepository
    ) = MovieDetailsProjection(movieDetailsRepository)
}