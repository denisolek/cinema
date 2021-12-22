package com.denisolek.cinema.infrastructure.persistence.mongo

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.denisolek.cinema.domain.shared.IOError
import com.denisolek.cinema.domain.shared.MovieId
import com.denisolek.cinema.domain.shared.ShowId
import com.denisolek.cinema.domain.show.infrastructure.ShowRepository
import com.denisolek.cinema.domain.show.model.Currency.valueOf
import com.denisolek.cinema.domain.show.model.Price
import com.denisolek.cinema.domain.show.model.Show
import com.denisolek.cinema.domain.show.model.Showtime.Companion.persistedShowtime
import com.denisolek.cinema.infrastructure.persistence.mongo.MongoClientFactory.mongodb
import com.denisolek.cinema.infrastructure.persistence.mongo.MongoCollections.SHOW
import mu.KotlinLogging.logger
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.*
import java.math.BigDecimal
import java.time.Instant
import java.util.*

class MongoShowRepository : ShowRepository {
    private val log = logger {}
    private val collection = mongodb().getCollection<ShowDocument>(SHOW)

    override fun save(show: Show): Either<IOError, Unit> = catch {
        collection.save(show.toDocument())
    }.mapLeft { ex: Throwable ->
        log.mongoError(ex)
        ex.mapToMongoFailure("${show.id}")
    }

    override fun notContainsOverlappingShow(newStart: Instant, newEnd: Instant): Either<IOError, Boolean> = catch {
        // count can be replaced with finding first matching document
        val result = collection.countDocuments(and(
            ShowDocument::start lte newEnd,
            ShowDocument::end gte newStart
        ))
        if (result != 0L) throw DataIntegrityException("overlapping shows")
        true
    }.mapLeft { ex: Throwable ->
        log.mongoError(ex)
        ex.mapToMongoFailure("$newStart - $newEnd")
    }

    override fun findAll(): Either<IOError, List<Show>> = catch {
        collection.find().map { it.toDomain() }.toList()
    }.mapLeft { ex: Throwable ->
        log.mongoError(ex)
        ex.mapToMongoFailure("find all")
    }

    override fun find(showId: ShowId): Either<IOError, Show> = catch {
        collection.findOneById(showId.value)?.toDomain() ?: throw NotFoundException("${showId.value}")
    }.mapLeft { ex: Throwable ->
        log.mongoError(ex)
        ex.mapToMongoFailure("${showId.value}")
    }


    override fun remove(showId: ShowId): Either<IOError, Unit> = catch {
        val result = collection.deleteOneById(showId.value)
        if (result.deletedCount != 1L) throw DeleteException("${showId.value}")
    }.mapLeft { ex: Throwable ->
        log.mongoError(ex)
        ex.mapToMongoFailure("${showId.value}")
    }
}

data class ShowDocument(
    @BsonId val id: UUID,
    val movieId: String,
    val start: Instant,
    val end: Instant,
    val duration: Long,
    val price: BigDecimal,
    val currency: String,
)

private fun Show.toDocument() = ShowDocument(
    id = id.value,
    movieId = movieId.value,
    start = showtime.start,
    end = showtime.end,
    duration = showtime.minutes,
    price = price.amount,
    currency = price.currency.name
)

private fun ShowDocument.toDomain() = Show(
    id = ShowId(id),
    movieId = MovieId(movieId),
    showtime = persistedShowtime(start, duration),
    price = Price.persistedPrice(price, valueOf(currency))
)
