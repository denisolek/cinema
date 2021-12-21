package com.denisolek.cinema.infrastructure.persistance

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.denisolek.cinema.domain.readmodel.infrastructure.ShowScheduleRepository
import com.denisolek.cinema.domain.readmodel.model.ShowSchedule
import com.denisolek.cinema.domain.shared.IOError
import com.denisolek.cinema.domain.shared.IOError.NotFound
import mu.KotlinLogging.logger
import java.time.Instant
import java.util.*

class InMemoryShowScheduleRepository : ShowScheduleRepository {
    private val log = logger {}
    private val schedules: MutableMap<UUID, ShowSchedule> = mutableMapOf()

    override fun save(show: ShowSchedule): Either<IOError, ShowSchedule> {
        schedules[show.showId] = show
        return show.right().also { log.info("Saved $it") }
    }

    override fun find(showId: UUID): Either<IOError, ShowSchedule> {
        return schedules[showId]?.right() ?: NotFound("$showId").left()
    }

    override fun remove(showId: UUID): Either<IOError, Unit> {
        schedules.remove(showId)
        return Unit.right()
    }

    override fun findAll(movieId: String): Either<IOError, List<ShowSchedule>> {
        return schedules.values.filter { it.movieId == movieId }.right()
    }

    override fun findAll(from: Instant, to: Instant): Either<IOError, List<ShowSchedule>> {
        return schedules.values.filter { it.start.isAfterOrEqual(from) && it.start.isBeforeOrEqual(to) }.right()
    }

    override fun findAll(movieId: String, from: Instant, to: Instant): Either<IOError, List<ShowSchedule>> {
        return schedules.values.filter { it.movieId == movieId && it.start.isAfterOrEqual(from) && it.start.isBeforeOrEqual(to) }.right()
    }

    fun clear() = schedules.clear()
}