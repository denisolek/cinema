package com.denisolek.cinema.infrastructure.persistance.mongo

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.denisolek.cinema.application.config.DefaultUser
import com.denisolek.cinema.domain.authentication.Authentication
import com.denisolek.cinema.domain.authentication.Role.valueOf
import com.denisolek.cinema.domain.authentication.infrastructure.AuthenticationRepository
import com.denisolek.cinema.domain.shared.IOError
import com.denisolek.cinema.domain.shared.UserId
import com.denisolek.cinema.infrastructure.persistance.mongo.MongoClientFactory.mongodb
import mu.KotlinLogging.logger
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.findOneById
import org.litote.kmongo.getCollection
import org.litote.kmongo.save

class MongoAuthenticationRepository(defaultUsers: List<DefaultUser>) : AuthenticationRepository {
    private val log = logger {}
    private val collection = mongodb().getCollection<AuthenticationDocument>("Authentication")

    init {
        defaultUsers.forEach { collection.save(it.toDocument()) }
    }

    override fun find(token: String): Either<IOError, Authentication> = catch {
        collection.findOneById(token)?.toDomain() ?: throw NotFoundException(token)
    }.mapLeft { ex: Throwable ->
        log.mongoError(ex)
        ex.mapToMongoFailure(token)
    }
}

data class AuthenticationDocument(
    @BsonId val token: String,
    val userId: String,
    val roles: Set<String>,
)

private fun DefaultUser.toDocument() = AuthenticationDocument(
    token = token,
    userId = userId,
    roles = roles
)

private fun AuthenticationDocument.toDomain() = Authentication(
    id = UserId(userId),
    roles = roles.map { valueOf(it) }.toSet()
)