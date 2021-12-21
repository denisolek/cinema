package com.denisolek.cinema.application.config

import com.denisolek.cinema.domain.movie.MovieFacade
import com.denisolek.cinema.domain.readmodel.MovieDetailsProjection
import com.denisolek.cinema.domain.readmodel.ShowScheduleProjection
import com.denisolek.cinema.domain.readmodel.infrastructure.MovieDetailsRepository
import com.denisolek.cinema.domain.readmodel.infrastructure.ShowScheduleRepository
import com.denisolek.cinema.infrastructure.persistance.InMemoryMovieDetailsRepository
import com.denisolek.cinema.infrastructure.persistance.InMemoryShowScheduleRepository
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


    @Bean
    fun showScheduleRepository() = InMemoryShowScheduleRepository()

    @Bean
    fun showScheduleProjection(
        showScheduleRepository: ShowScheduleRepository,
        movieFacade: MovieFacade
    ) = ShowScheduleProjection(showScheduleRepository, movieFacade)
}