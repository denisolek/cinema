package com.denisolek.cinema.api

import com.denisolek.cinema.domain.shared.Failure
import com.denisolek.cinema.domain.shared.IOError
import com.denisolek.cinema.domain.shared.IOError.*
import com.denisolek.cinema.domain.shared.ValidationError
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.status

fun Failure.mapToResponseFailure(): ResponseEntity<Any> = when (this) {
    is ValidationError -> UNPROCESSABLE_ENTITY
    is IOError -> when (this) {
        is NotFound -> NOT_FOUND
        is Unauthorized -> UNAUTHORIZED
        is Unavailable -> SERVICE_UNAVAILABLE
        is ClientFailure -> BAD_REQUEST
        is UnknownFailure -> INTERNAL_SERVER_ERROR
    }
}.let { statusCode -> status(statusCode).body("{\"reason\": \"$reason\"}") }