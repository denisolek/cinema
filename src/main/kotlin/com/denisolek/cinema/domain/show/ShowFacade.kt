package com.denisolek.cinema.domain.show

import arrow.core.Either
import arrow.core.computations.either.eager
import com.denisolek.cinema.domain.authentication.Role.OWNER
import com.denisolek.cinema.domain.movie.MovieFacade
import com.denisolek.cinema.domain.shared.Failure
import com.denisolek.cinema.domain.shared.event.DomainEventPublisher
import com.denisolek.cinema.domain.show.infrastructure.ShowRepository
import com.denisolek.cinema.domain.show.model.Show.Companion.show

class ShowFacade(
    private val repository: ShowRepository,
    private val movieFacade: MovieFacade,
    private val eventPublisher: DomainEventPublisher
) {
    fun addShow(command: AddShow): Either<Failure, Unit> = eager {
        command.authentication.sufficientFor(OWNER).bind()
        val runtimeInfo = movieFacade.getRuntimeInfo(command.movieId).bind()
        repository.notContainsOverlappingShow(command.start, command.start.plus(runtimeInfo.runtime)).bind()
        val result = show(command, runtimeInfo.runtime).bind()
        repository.save(result.show)
        eventPublisher.publish(result.events)
    }

    fun updateShow(): Either<Failure, Unit> = TODO()
    fun removeShow(): Either<Failure, Unit> = TODO()

    fun showInfos(): Either<Failure, List<ShowInfo>> = eager {
        val shows = repository.findAll().bind()
        shows.map { ShowInfo(it) }
    }
}