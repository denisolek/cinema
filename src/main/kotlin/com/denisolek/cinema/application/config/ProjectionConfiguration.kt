package com.denisolek.cinema.application.config

import com.denisolek.cinema.domain.movie.MovieFacade
import com.denisolek.cinema.domain.readmodel.MovieDetailsProjection
import com.denisolek.cinema.domain.readmodel.ShowScheduleProjection
import com.denisolek.cinema.domain.readmodel.infrastructure.MovieDetailsRepository
import com.denisolek.cinema.domain.readmodel.infrastructure.ShowScheduleRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ProjectionConfiguration {

    @Bean
    fun movieDetailsProjection(movieDetailsRepository: MovieDetailsRepository) = MovieDetailsProjection(movieDetailsRepository)

    @Bean
    fun showScheduleProjection(
        showScheduleRepository: ShowScheduleRepository,
        movieFacade: MovieFacade,
    ) = ShowScheduleProjection(showScheduleRepository, movieFacade)
}