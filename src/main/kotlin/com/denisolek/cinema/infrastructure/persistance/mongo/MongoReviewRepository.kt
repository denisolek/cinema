package com.denisolek.cinema.infrastructure.persistance.mongo

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.denisolek.cinema.domain.review.infrastructure.ReviewRepository
import com.denisolek.cinema.domain.review.model.Review
import com.denisolek.cinema.domain.review.model.Stars.Companion.persistedStars
import com.denisolek.cinema.domain.shared.IOError
import com.denisolek.cinema.domain.shared.MovieId
import com.denisolek.cinema.domain.shared.ReviewId
import com.denisolek.cinema.domain.shared.UserId
import com.denisolek.cinema.infrastructure.persistance.mongo.MongoClientFactory.mongodb
import mu.KotlinLogging.logger
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.*
import java.time.Instant
import java.util.*

class MongoReviewRepository : ReviewRepository {
    private val log = logger {}
    private val collection = mongodb().getCollection<ReviewDocument>("Review")

    override fun save(review: Review): Either<IOError, Unit> = catch {
        collection.save(review.toDocument())
    }.mapLeft { ex: Throwable ->
        log.mongoError(ex)
        ex.mapToMongoFailure("${review.id.value}")
    }

    override fun find(reviewId: ReviewId): Either<IOError, Review> = catch {
        collection.findOneById(reviewId.value)?.toDomain() ?: throw NotFoundException("${reviewId.value}")
    }.mapLeft { ex: Throwable ->
        log.mongoError(ex)
        ex.mapToMongoFailure("${reviewId.value}")
    }

    override fun find(userId: UserId, movieId: MovieId): Either<IOError, Review> = catch {
        val result = collection.find(
            ReviewDocument::userId eq userId.value,
            ReviewDocument::movieId eq movieId.value
        ).map { it.toDomain() }.toList()
        if (result.size > 1) throw DataIntegrityException("Found ${result.size} reviews")
        if (result.isEmpty()) throw NotFoundException("$userId $movieId")
        result.first()
    }.mapLeft { ex: Throwable ->
        log.mongoError(ex)
        ex.mapToMongoFailure("${userId.value} ${movieId.value}")
    }

    override fun findAll(movieId: MovieId): Either<IOError, List<Review>> = catch {
        collection.find(ReviewDocument::movieId eq movieId.value)
            .map { it.toDomain() }.toList()
    }.mapLeft { ex: Throwable ->
        log.mongoError(ex)
        ex.mapToMongoFailure(movieId.value)
    }
}

data class ReviewDocument(
    @BsonId val id: UUID,
    val movieId: String,
    val userId: String,
    val date: Instant,
    val stars: Int,
)

private fun Review.toDocument() = ReviewDocument(
    id = id.value,
    movieId = movieId.value,
    userId = userId.value,
    date = date,
    stars = stars.value
)

private fun ReviewDocument.toDomain() = Review(
    id = ReviewId(id),
    movieId = MovieId(movieId),
    userId = UserId(userId),
    date = date,
    stars = persistedStars(stars)
)