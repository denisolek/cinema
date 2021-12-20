package com.denisolek.cinema.domain.show.infrastructure

import arrow.core.Either
import com.denisolek.cinema.domain.shared.IOError
import com.denisolek.cinema.domain.shared.ShowId
import com.denisolek.cinema.domain.show.model.Show
import java.time.Instant

interface ShowRepository {
    fun notContainsOverlappingShow(newStart: Instant, newEnd: Instant): Either<IOError, Boolean>
    fun save(show: Show): Either<IOError, Show>
    fun findAll(): Either<IOError, List<Show>>
    fun find(showId: ShowId): Either<IOError, Show>
    fun remove(showId: ShowId): Either<IOError, Unit>
}
