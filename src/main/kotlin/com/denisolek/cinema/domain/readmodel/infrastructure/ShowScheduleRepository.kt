package com.denisolek.cinema.domain.readmodel.infrastructure

import arrow.core.Either
import com.denisolek.cinema.domain.readmodel.model.ShowSchedule
import com.denisolek.cinema.domain.shared.IOError
import java.time.Instant
import java.util.*

interface ShowScheduleRepository {
    fun save(show: ShowSchedule): Either<IOError, ShowSchedule>
    fun find(showId: UUID): Either<IOError, ShowSchedule>
    fun remove(showId: UUID): Either<IOError, Unit>
    fun findAll(movieId: String): Either<IOError, List<ShowSchedule>>
    fun findAll(from: Instant, to: Instant): Either<IOError, List<ShowSchedule>>
    fun findAll(movieId: String, from: Instant, to: Instant): Either<IOError, List<ShowSchedule>>
}