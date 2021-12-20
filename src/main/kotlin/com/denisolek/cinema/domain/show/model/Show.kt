package com.denisolek.cinema.domain.show.model

import arrow.core.Either
import arrow.core.computations.either.eager
import com.denisolek.cinema.domain.shared.Failure
import com.denisolek.cinema.domain.shared.MovieId
import com.denisolek.cinema.domain.shared.ShowId
import com.denisolek.cinema.domain.shared.event.DomainEvent
import com.denisolek.cinema.domain.show.AddShow
import java.time.Duration

data class Show(
    val id: ShowId,
    val movieId: MovieId,
    val showtime: Showtime,
    val price: Price
) {

    companion object {
        fun show(command: AddShow, runtime: Duration): Either<Failure, ShowOperation> = eager {
            Show(
                id = ShowId(),
                movieId = command.movieId,
                showtime = Showtime.newShowtime(command.start, runtime).bind(),
                price = Price.price(command.price).bind()
            ).let { ShowOperation(it, listOf(ShowAdded(it))) }
        }
    }
}

class ShowOperation(val show: Show, val events: List<DomainEvent>)