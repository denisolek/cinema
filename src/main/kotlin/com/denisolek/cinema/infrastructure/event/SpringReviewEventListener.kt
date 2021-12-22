package com.denisolek.cinema.infrastructure.event

import com.denisolek.cinema.domain.review.ReviewEventHandler
import com.denisolek.cinema.domain.review.infrastructure.ReviewEventListener
import com.denisolek.cinema.domain.review.model.ReviewAdded
import com.denisolek.cinema.domain.review.model.ReviewUpdated
import com.denisolek.cinema.domain.shared.event.DomainEvent
import mu.KotlinLogging.logger
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class SpringReviewEventListener(private val handler: ReviewEventHandler) : ReviewEventListener {
    private val log = logger {}

    @EventListener
    override fun handle(event: DomainEvent) {
        log.debug { "Received $event" }
        when (event) {
            is ReviewAdded -> handler.handle(event)
            is ReviewUpdated -> handler.handle(event)
        }
    }
}