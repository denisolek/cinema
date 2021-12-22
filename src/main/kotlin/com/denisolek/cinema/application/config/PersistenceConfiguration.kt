package com.denisolek.cinema.application.config

import com.denisolek.cinema.domain.authentication.infrastructure.AuthenticationRepository
import com.denisolek.cinema.domain.movie.infrastructure.MovieRepository
import com.denisolek.cinema.domain.readmodel.infrastructure.MovieDetailsRepository
import com.denisolek.cinema.domain.readmodel.infrastructure.ShowScheduleRepository
import com.denisolek.cinema.domain.review.infrastructure.ReviewRepository
import com.denisolek.cinema.domain.show.infrastructure.ShowRepository
import com.denisolek.cinema.infrastructure.ApplicationProperties
import com.denisolek.cinema.infrastructure.persistance.inmemory.*
import com.denisolek.cinema.infrastructure.persistance.mongo.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PersistenceConfiguration(val properties: ApplicationProperties) {

    @Bean
    fun authenticationRepository(): AuthenticationRepository = datasource(
        inMemory = { InMemoryAuthenticationRepository(defaultUsers) },
        mongo = { MongoAuthenticationRepository(defaultUsers) }
    )

    @Bean
    fun movieRepository(): MovieRepository = datasource(
        inMemory = { InMemoryMovieRepository() },
        mongo = { MongoMovieRepository() }
    )

    @Bean
    fun movieDetailsRepository(): MovieDetailsRepository = datasource(
        inMemory = { InMemoryMovieDetailsRepository() },
        mongo = { MongoMovieDetailsRepository() }
    )

    @Bean
    fun showScheduleRepository(): ShowScheduleRepository = datasource(
        inMemory = { InMemoryShowScheduleRepository() },
        mongo = { MongoShowScheduleRepository() }
    )

    @Bean
    fun reviewRepository(): ReviewRepository = datasource(
        inMemory = { InMemoryReviewRepository() },
        mongo = { MongoReviewRepository() }
    )

    @Bean
    fun showRepository(): ShowRepository = datasource(
        inMemory = { InMemoryShowRepository() },
        mongo = { MongoShowRepository() }
    )

    fun <T> datasource(inMemory: () -> T, mongo: () -> T): T = if (properties.useMongo) mongo() else inMemory()

    val defaultUsers = listOf(
        DefaultUser("owner-token", "owner-id", setOf("OWNER", "MOVIEGOER")),
        DefaultUser("moviegoer-token", "moviegoer-id", setOf("MOVIEGOER")),
        DefaultUser("system-token", "system-id", setOf("APPLICATION"))
    )
}