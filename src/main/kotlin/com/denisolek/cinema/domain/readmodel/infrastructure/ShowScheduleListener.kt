package com.denisolek.cinema.domain.readmodel.infrastructure

import com.denisolek.cinema.domain.shared.event.DomainEvent

interface ShowScheduleListener {
    fun handle(event: DomainEvent)
}