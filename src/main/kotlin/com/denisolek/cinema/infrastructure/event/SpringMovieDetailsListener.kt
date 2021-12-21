package com.denisolek.cinema.infrastructure.event

import com.denisolek.cinema.domain.movie.model.MovieLoaded
import com.denisolek.cinema.domain.readmodel.MovieDetailsProjection
import com.denisolek.cinema.domain.readmodel.infrastructure.MovieDetailsListener
import com.denisolek.cinema.domain.review.model.SummedReviewChanged
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class SpringMovieDetailsListener(val projection: MovieDetailsProjection) : MovieDetailsListener {

    @EventListener
    override fun handle(event: MovieLoaded) {
        projection.handle(event)
    }

    @EventListener
    override fun handle(event: SummedReviewChanged) {
        projection.handle(event)
    }
}