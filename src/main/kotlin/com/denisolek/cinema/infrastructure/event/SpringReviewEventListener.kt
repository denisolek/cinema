package com.denisolek.cinema.infrastructure.event

import com.denisolek.cinema.domain.review.ReviewEventHandler
import com.denisolek.cinema.domain.review.model.ReviewAdded
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class SpringReviewEventListener(val handler: ReviewEventHandler) {

    @EventListener
    fun handle(event: ReviewAdded) {
        handler.handle(event)
    }
}