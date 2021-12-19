package com.denisolek.cinema.domain.movie

import arrow.core.Either
import arrow.core.computations.either.eager
import com.denisolek.cinema.domain.movie.infrastructure.MovieDataRepository
import com.denisolek.cinema.domain.movie.infrastructure.MovieRepository
import com.denisolek.cinema.domain.movie.model.Movie.Companion.movie
import com.denisolek.cinema.domain.movie.model.MovieLoaded.Companion.movieLoaded
import com.denisolek.cinema.domain.shared.Failure
import com.denisolek.cinema.domain.shared.MovieId
import com.denisolek.cinema.domain.shared.event.DomainEventPublisher

class MovieFacade(
    private val repository: MovieRepository,
    private val movieDataRepository: MovieDataRepository,
    private val eventPublisher: DomainEventPublisher
) {
    fun loadMovies(movieIds: List<MovieId>): Either<Failure, Unit> = eager {
        val movieData = movieDataRepository.find(movieIds).bind()
        val movies = movieData.map { movie(it).bind() }
        movies.forEach {
            repository.save(it)
            eventPublisher.publish(movieLoaded(it))
        }
    }
}