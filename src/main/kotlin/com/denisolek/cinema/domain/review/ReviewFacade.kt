package com.denisolek.cinema.domain.review

import arrow.core.Either
import arrow.core.computations.either.eager
import arrow.core.left
import arrow.core.right
import com.denisolek.cinema.domain.authentication.Role.MOVIEGOER
import com.denisolek.cinema.domain.movie.MovieFacade
import com.denisolek.cinema.domain.review.infrastructure.ReviewRepository
import com.denisolek.cinema.domain.review.model.Review.Companion.review
import com.denisolek.cinema.domain.shared.Failure
import com.denisolek.cinema.domain.shared.IOError.NotFound
import com.denisolek.cinema.domain.shared.event.DomainEventPublisher

class ReviewFacade(
    private val repository: ReviewRepository,
    private val movieFacade: MovieFacade,
    private val eventPublisher: DomainEventPublisher,
) {

    fun addReview(command: AddReview): Either<Failure, Unit> = eager {
        command.authentication.sufficientFor(MOVIEGOER).bind()
        movieFacade.ensureMovieExists(command.movieId).bind()
        val existingReview = repository.find(command.authentication.id, command.movieId)
        val result = existingReview.fold(
            ifLeft = {
                if (it is NotFound) review(command).right().bind()
                else it.left().bind()
            },
            ifRight = { it.update(command).right().bind() }
        ).bind()

        if (result.events.isNotEmpty()) {
            repository.save(result.review)
            eventPublisher.publish(result.events)
        }
    }
}