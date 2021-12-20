package com.denisolek.cinema.infrastructure.http

import com.denisolek.cinema.domain.shared.IOError.*
import io.ktor.client.features.*
import io.ktor.http.HttpStatusCode.Companion.Unauthorized

fun Throwable.mapToHttpFailure(resource: String) = when (this) {
    is ClientRequestException -> when (this.response.status) {
        Unauthorized -> UnauthorizedAccess(resource)
        else -> ClientFailure(resource)
    }
    is ServerResponseException -> Unavailable(resource)
    else -> UnknownFailure(resource)
}