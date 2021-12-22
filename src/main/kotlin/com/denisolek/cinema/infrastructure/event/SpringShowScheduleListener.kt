package com.denisolek.cinema.infrastructure.event

import com.denisolek.cinema.domain.movie.model.MovieLoaded
import com.denisolek.cinema.domain.readmodel.ShowScheduleProjection
import com.denisolek.cinema.domain.readmodel.infrastructure.ShowScheduleListener
import com.denisolek.cinema.domain.shared.event.DomainEvent
import com.denisolek.cinema.domain.show.model.ShowAdded
import com.denisolek.cinema.domain.show.model.ShowPriceUpdated
import com.denisolek.cinema.domain.show.model.ShowRemoved
import com.denisolek.cinema.domain.show.model.ShowTimeUpdated
import mu.KotlinLogging.logger
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class SpringShowScheduleListener(val projection: ShowScheduleProjection) : ShowScheduleListener {
    private val log = logger {}

    @EventListener
    override fun handle(event: DomainEvent) {
        log.debug { "Received $event" }
        when (event) {
            is ShowAdded -> projection.handle(event)
            is ShowTimeUpdated -> projection.handle(event)
            is ShowPriceUpdated -> projection.handle(event)
            is ShowRemoved -> projection.handle(event)
            is MovieLoaded -> projection.handle(event)
        }
    }
}