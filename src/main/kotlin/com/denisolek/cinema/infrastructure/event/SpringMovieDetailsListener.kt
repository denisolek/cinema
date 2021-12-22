package com.denisolek.cinema.infrastructure.event

import com.denisolek.cinema.domain.movie.model.MovieLoaded
import com.denisolek.cinema.domain.readmodel.MovieDetailsProjection
import com.denisolek.cinema.domain.readmodel.infrastructure.MovieDetailsListener
import com.denisolek.cinema.domain.review.model.SummedReviewChanged
import com.denisolek.cinema.domain.shared.event.DomainEvent
import mu.KotlinLogging.logger
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class SpringMovieDetailsListener(private val projection: MovieDetailsProjection) : MovieDetailsListener {
    private val log = logger {}

    @EventListener
    override fun handle(event: DomainEvent) {
        log.debug { "Received $event" }
        when (event) {
            is MovieLoaded -> projection.handle(event)
            is SummedReviewChanged -> projection.handle(event)
        }
    }
}