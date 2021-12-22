package com.denisolek.cinema.infrastructure.persistence.mongo

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.denisolek.cinema.domain.readmodel.infrastructure.MovieDetailsRepository
import com.denisolek.cinema.domain.readmodel.model.MovieDetails
import com.denisolek.cinema.domain.shared.IOError
import com.denisolek.cinema.infrastructure.persistence.mongo.MongoClientFactory.mongodb
import com.denisolek.cinema.infrastructure.persistence.mongo.MongoCollections.MOVIE_DETAILS
import mu.KotlinLogging.logger
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.findOneById
import org.litote.kmongo.getCollection
import org.litote.kmongo.save
import java.time.Duration.ofMinutes
import java.time.Instant

class MongoMovieDetailsRepository : MovieDetailsRepository {
    private val log = logger {}
    private val collection = mongodb().getCollection<MovieDetailsDocument>(MOVIE_DETAILS)

    override fun save(movie: MovieDetails): Either<IOError, Unit> = catch {
        collection.save(movie.toDocument())
    }.mapLeft { ex: Throwable ->
        log.mongoError(ex)
        ex.mapToMongoFailure(movie.id)
    }

    override fun find(id: String): Either<IOError, MovieDetails> = catch {
        collection.findOneById(id)?.toDomain() ?: throw NotFoundException(id)
    }.mapLeft { ex: Throwable ->
        log.mongoError(ex)
        ex.mapToMongoFailure(id)
    }
}

data class MovieDetailsDocument(
    @BsonId val id: String,
    val title: String,
    val description: String,
    val releaseDate: Instant,
    val awards: String,
    val internalRating: Double,
    val internalVotes: Int,
    val imdbRating: Double,
    val imdbVotes: Int,
    val runtime: Long,
)

private fun MovieDetails.toDocument() = MovieDetailsDocument(
    id = id,
    title = title,
    description = description,
    releaseDate = releaseDate,
    awards = awards,
    internalRating = internalRating,
    internalVotes = internalVotes,
    imdbRating = imdbRating,
    imdbVotes = imdbVotes,
    runtime = runtime.toMinutes()
)

private fun MovieDetailsDocument.toDomain() = MovieDetails(
    id = id,
    title = title,
    description = description,
    releaseDate = releaseDate,
    awards = awards,
    internalRating = internalRating,
    internalVotes = internalVotes,
    imdbRating = imdbRating,
    imdbVotes = imdbVotes,
    runtime = ofMinutes(runtime)
)