package com.denisolek.cinema.infrastructure.event

import com.denisolek.cinema.domain.review.ReviewEventHandler
import com.denisolek.cinema.domain.review.infrastructure.ReviewEventListener
import com.denisolek.cinema.domain.review.model.ReviewAdded
import com.denisolek.cinema.domain.review.model.ReviewUpdated
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class SpringReviewEventListener(val handler: ReviewEventHandler) : ReviewEventListener {

    @EventListener
    override fun handle(event: ReviewAdded) {
        handler.handle(event)
    }

    @EventListener
    override fun handle(event: ReviewUpdated) {
        handler.handle(event)
    }
}