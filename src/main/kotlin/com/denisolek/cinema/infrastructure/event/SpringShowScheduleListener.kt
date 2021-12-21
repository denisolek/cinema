package com.denisolek.cinema.infrastructure.event

import com.denisolek.cinema.domain.movie.model.MovieLoaded
import com.denisolek.cinema.domain.readmodel.ShowScheduleProjection
import com.denisolek.cinema.domain.readmodel.infrastructure.ShowScheduleListener
import com.denisolek.cinema.domain.show.model.ShowAdded
import com.denisolek.cinema.domain.show.model.ShowPriceUpdated
import com.denisolek.cinema.domain.show.model.ShowRemoved
import com.denisolek.cinema.domain.show.model.ShowTimeUpdated
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class SpringShowScheduleListener(val projection: ShowScheduleProjection) : ShowScheduleListener {

    @EventListener
    override fun handle(event: ShowAdded) {
        projection.handle(event)
    }

    @EventListener
    override fun handle(event: ShowTimeUpdated) {
        projection.handle(event)
    }

    @EventListener
    override fun handle(event: ShowPriceUpdated) {
        projection.handle(event)
    }

    @EventListener
    override fun handle(event: ShowRemoved) {
        projection.handle(event)
    }

    @EventListener
    override fun handle(event: MovieLoaded) {
        projection.handle(event)
    }
}