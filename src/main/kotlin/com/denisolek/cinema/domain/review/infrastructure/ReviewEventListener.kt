package com.denisolek.cinema.domain.review.infrastructure

import com.denisolek.cinema.domain.shared.event.DomainEvent

interface ReviewEventListener {
    fun handle(event: DomainEvent)
}