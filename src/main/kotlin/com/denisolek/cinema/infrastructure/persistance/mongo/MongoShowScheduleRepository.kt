package com.denisolek.cinema.infrastructure.persistance.mongo

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.denisolek.cinema.domain.readmodel.infrastructure.ShowScheduleRepository
import com.denisolek.cinema.domain.readmodel.model.ShowSchedule
import com.denisolek.cinema.domain.shared.IOError
import com.denisolek.cinema.infrastructure.persistance.mongo.MongoClientFactory.mongodb
import mu.KotlinLogging.logger
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.*
import java.time.Duration.ofMinutes
import java.time.Instant
import java.util.*

class MongoShowScheduleRepository : ShowScheduleRepository {
    private val log = logger {}
    private val collection = mongodb().getCollection<ShowScheduleDocument>("ShowSchedule")

    override fun save(show: ShowSchedule): Either<IOError, Unit> = catch {
        collection.save(show.toDocument())
    }.mapLeft { ex: Throwable ->
        log.mongoError(ex)
        ex.mapToMongoFailure("${show.showId}")
    }

    override fun find(showId: UUID): Either<IOError, ShowSchedule> = catch {
        collection.findOneById(showId)?.toDomain() ?: throw NotFoundException("$showId")
    }.mapLeft { ex: Throwable ->
        log.mongoError(ex)
        ex.mapToMongoFailure("$showId")
    }

    override fun remove(showId: UUID): Either<IOError, Unit> = catch {
        val result = collection.deleteOneById(showId)
        if (result.deletedCount != 1L) throw DeleteException("$showId")
    }.mapLeft { ex: Throwable ->
        log.mongoError(ex)
        ex.mapToMongoFailure("$showId")
    }

    override fun findAll(movieId: String): Either<IOError, List<ShowSchedule>> = catch {
        collection.find(ShowScheduleDocument::movieId eq movieId).map { it.toDomain() }.toList()
    }.mapLeft { ex: Throwable ->
        log.mongoError(ex)
        ex.mapToMongoFailure("by movie $movieId")
    }

    override fun findAll(from: Instant, to: Instant): Either<IOError, List<ShowSchedule>> = catch {
        collection.find(
            ShowScheduleDocument::start gt from,
            ShowScheduleDocument::start lt to,
        ).map { it.toDomain() }.toList()
    }.mapLeft { ex: Throwable ->
        log.mongoError(ex)
        ex.mapToMongoFailure("$from - $to")
    }

    override fun findAll(movieId: String, from: Instant, to: Instant): Either<IOError, List<ShowSchedule>> = catch {
        collection.find(
            ShowScheduleDocument::movieId eq movieId,
            ShowScheduleDocument::start gt from,
            ShowScheduleDocument::start lt to,
        ).map { it.toDomain() }.toList()
    }.mapLeft { ex: Throwable ->
        log.mongoError(ex)
        ex.mapToMongoFailure("$from - $to")
    }
}

data class ShowScheduleDocument(
    @BsonId val showId: UUID,
    val start: Instant,
    val end: Instant,
    val price: Double,
    val currency: String,
    val movieId: String,
    val movieTitle: String,
    val movieDescription: String,
    val movieRuntime: Long,
)

private fun ShowSchedule.toDocument() = ShowScheduleDocument(
    showId = showId,
    start = start,
    end = end,
    price = price,
    currency = currency,
    movieId = movieId,
    movieTitle = movieTitle,
    movieDescription = movieDescription,
    movieRuntime = movieRuntime.toMinutes()
)

private fun ShowScheduleDocument.toDomain() = ShowSchedule(
    showId = showId,
    start = start,
    end = end,
    price = price,
    currency = currency,
    movieId = movieId,
    movieTitle = movieTitle,
    movieDescription = movieDescription,
    movieRuntime = ofMinutes(movieRuntime)
)