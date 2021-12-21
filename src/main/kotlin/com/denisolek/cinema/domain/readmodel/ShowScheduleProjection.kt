package com.denisolek.cinema.domain.readmodel

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.left
import com.denisolek.cinema.domain.movie.MovieFacade
import com.denisolek.cinema.domain.movie.model.MovieLoaded
import com.denisolek.cinema.domain.readmodel.infrastructure.ShowScheduleRepository
import com.denisolek.cinema.domain.readmodel.model.MovieShow
import com.denisolek.cinema.domain.readmodel.model.MovieShows
import com.denisolek.cinema.domain.readmodel.model.ShowSchedule
import com.denisolek.cinema.domain.shared.IOError
import com.denisolek.cinema.domain.shared.IOError.NotFound
import com.denisolek.cinema.domain.shared.MovieId
import com.denisolek.cinema.domain.show.model.ShowAdded
import com.denisolek.cinema.domain.show.model.ShowPriceUpdated
import com.denisolek.cinema.domain.show.model.ShowRemoved
import com.denisolek.cinema.domain.show.model.ShowTimeUpdated

class ShowScheduleProjection(
    val repository: ShowScheduleRepository,
    val movieFacade: MovieFacade
) {
    fun handle(event: ShowAdded) {
        movieFacade.getListingInfo(MovieId(event.movieId)).map { movie ->
            repository.save(ShowSchedule(event, movie))
        }
    }

    fun handle(event: ShowTimeUpdated) {
        repository.find(event.showId).map {
            repository.save(it.apply(event))
        }
    }

    fun handle(event: ShowPriceUpdated) {
        repository.find(event.showId).map {
            repository.save(it.apply(event))
        }
    }

    fun handle(event: ShowRemoved) {
        repository.remove(event.showId)
    }

    fun handle(event: MovieLoaded) {
        repository.findAll(event.movieId).map { schedules ->
            schedules.forEach {
                repository.save(it.apply(event))
            }
        }
    }

    fun query(query: FindShowSchedules): Either<IOError, List<ShowSchedule>> = repository.findAll(query.from, query.to)

    fun query(query: FindMovieShows): Either<IOError, MovieShows> = either.eager {
        val schedules = repository.findAll(query.movieId, query.from, query.to).bind()
        if (schedules.isEmpty()) NotFound("schedule for $query").left().bind()
        val baseSchedule = schedules.first()
        MovieShows(
            movieId = baseSchedule.movieId,
            title = baseSchedule.movieTitle,
            description = baseSchedule.movieDescription,
            runtime = baseSchedule.movieRuntime,
            shows = schedules.map { MovieShow(it) }
        )
    }
}