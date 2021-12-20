package com.denisolek.cinema.infrastructure.persistance

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.denisolek.cinema.domain.shared.IOError
import com.denisolek.cinema.domain.shared.IOError.DataIntegrityViolation
import com.denisolek.cinema.domain.shared.ShowId
import com.denisolek.cinema.domain.show.infrastructure.ShowRepository
import com.denisolek.cinema.domain.show.model.Show
import mu.KotlinLogging
import java.time.Instant

class InMemoryShowRepository : ShowRepository {
    private val log = KotlinLogging.logger {}
    private val shows: MutableMap<ShowId, Show> = mutableMapOf()

    // TODO use different model in real db repository to optimize requests
    override fun notContainsOverlappingShow(newStart: Instant, newEnd: Instant): Either<IOError, Boolean> {
        return shows.values.any {
            val existingStart = it.showtime.start
            val existingEnd = it.showtime.end
            existingStart.isBeforeOrEqual(newEnd) && existingEnd.isAfterOrEqual(newStart)
        }.let {
            if (it) DataIntegrityViolation("overlapping shows").left()
            else true.right()
        }
    }

    override fun save(show: Show): Either<IOError, Show> {
        shows[show.id] = show
        return show.right().also { log.info { "Saved $show" } }
    }

    override fun findAll(): Either<IOError, List<Show>> {
        return shows.values.toList().right()
    }
}

fun Instant.isBeforeOrEqual(other: Instant) = !this.isAfter(other)
fun Instant.isAfterOrEqual(other: Instant) = !this.isBefore(other)