package com.denisolek.cinema.domain.show.model

import arrow.core.Either
import arrow.core.computations.either.eager
import com.denisolek.cinema.domain.shared.Failure
import com.denisolek.cinema.domain.shared.MovieId
import com.denisolek.cinema.domain.shared.ShowId
import com.denisolek.cinema.domain.shared.event.DomainEvent
import com.denisolek.cinema.domain.show.AddShow
import com.denisolek.cinema.domain.show.UpdateShow
import com.denisolek.cinema.domain.show.model.Price.Companion.price
import com.denisolek.cinema.domain.show.model.Showtime.Companion.newShowtime
import java.time.Duration

data class Show(
    val id: ShowId,
    val movieId: MovieId,
    val showtime: Showtime,
    val price: Price
) {
    fun update(command: UpdateShow, runtime: Duration): Either<Failure, ShowOperation> = eager {
        val newShowtime = newShowtime(command.start, runtime).bind()
        val newPrice = price(command.price).bind()
        val events = listOfNotNull(
            if (showtime != newShowtime) ShowTimeUpdated(this@Show.copy(showtime = newShowtime)) else null,
            if (price != newPrice) ShowPriceUpdated(this@Show.copy(price = newPrice)) else null
        )
        ShowOperation(this@Show.copy(showtime = newShowtime, price = newPrice), events)
    }

    companion object {
        fun show(command: AddShow, runtime: Duration): Either<Failure, ShowOperation> = eager {
            Show(
                id = ShowId(),
                movieId = command.movieId,
                showtime = newShowtime(command.start, runtime).bind(),
                price = price(command.price).bind()
            ).let { ShowOperation(it, listOf(ShowAdded(it))) }
        }
    }
}

class ShowOperation(val show: Show, val events: List<DomainEvent>)