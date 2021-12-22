package com.denisolek.cinema.infrastructure.persistence.mongo

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.denisolek.cinema.domain.movie.infrastructure.MovieRepository
import com.denisolek.cinema.domain.movie.model.Movie
import com.denisolek.cinema.domain.movie.model.Runtime.Companion.persistedRuntime
import com.denisolek.cinema.domain.shared.IOError
import com.denisolek.cinema.domain.shared.MovieId
import com.denisolek.cinema.infrastructure.persistence.mongo.MongoClientFactory.mongodb
import com.denisolek.cinema.infrastructure.persistence.mongo.MongoCollections.MOVIE
import mu.KotlinLogging.logger
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.findOneById
import org.litote.kmongo.getCollection
import org.litote.kmongo.save
import java.time.Duration.ofMinutes

class MongoMovieRepository : MovieRepository {
    private val log = logger {}
    private val collection = mongodb().getCollection<MovieDocument>(MOVIE)

    override fun save(movie: Movie): Either<IOError, Unit> = catch {
        collection.save(movie.toDocument())
    }.mapLeft { ex: Throwable ->
        log.mongoError(ex)
        ex.mapToMongoFailure(movie.id.value)
    }

    override fun find(movieId: MovieId): Either<IOError, Movie> = catch {
        collection.findOneById(movieId.value)?.toDomain() ?: throw NotFoundException(movieId.value)
    }.mapLeft { ex: Throwable ->
        log.mongoError(ex)
        ex.mapToMongoFailure(movieId.value)
    }

    override fun findAll(): Either<IOError, List<Movie>> = catch {
        collection.find().toList().map { it.toDomain() }
    }.mapLeft { ex: Throwable ->
        log.mongoError(ex)
        ex.mapToMongoFailure("all movies")
    }
}

data class MovieDocument(
    @BsonId val id: String,
    val title: String,
    val description: String,
    val runtime: Long,
)

private fun Movie.toDocument() = MovieDocument(
    id = id.value,
    title = title,
    description = description,
    runtime = runtime.value.toMinutes()
)

private fun MovieDocument.toDomain() = Movie(
    id = MovieId(id),
    title = title,
    description = description,
    runtime = persistedRuntime(ofMinutes(runtime))
)