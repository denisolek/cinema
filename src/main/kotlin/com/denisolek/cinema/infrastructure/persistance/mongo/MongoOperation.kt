package com.denisolek.cinema.infrastructure.persistance.mongo

import com.denisolek.cinema.domain.shared.IOError.*
import com.mongodb.*
import mu.KLogger

fun Throwable.mapToMongoFailure(resource: String) = when (this) {
    is NotFoundException -> NotFound(resource)
    is MongoBulkWriteException,
    is MongoWriteException,
    is MongoWriteConcernException,
    is DataIntegrityException,
    -> DataIntegrityViolation(resource)
    is MongoExecutionTimeoutException,
    is MongoTimeoutException,
    is MongoSocketReadTimeoutException,
    is MongoServerUnavailableException,
    -> Unavailable(resource)
    else -> UnknownFailure(resource)
}

fun KLogger.mongoError(ex: Throwable) {
    if (ex is NotFoundException) {
        info { ex.message }
    } else {
        error(ex) { ex.message }
    }
}

class NotFoundException(resource: String) : Throwable("$resource not found")
class DeleteException(resource: String) : Throwable("$resource was not removed")
class DataIntegrityException(reason: String) : Throwable(reason)
