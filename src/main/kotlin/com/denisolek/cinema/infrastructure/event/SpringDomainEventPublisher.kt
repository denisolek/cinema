package com.denisolek.cinema.infrastructure.event

import com.denisolek.cinema.domain.shared.event.DomainEvent
import com.denisolek.cinema.domain.shared.event.DomainEventPublisher
import mu.KotlinLogging.logger
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class SpringDomainEventPublisher(private val applicationEventPublisher: ApplicationEventPublisher) : DomainEventPublisher {
    private val log = logger {}

    override fun publish(event: DomainEvent) {
        log.info { "Publishing $event" }
        applicationEventPublisher.publishEvent(event)
    }
}