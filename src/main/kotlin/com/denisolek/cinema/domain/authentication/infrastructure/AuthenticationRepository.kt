package com.denisolek.cinema.domain.authentication.infrastructure

import arrow.core.Either
import com.denisolek.cinema.domain.authentication.Authentication
import com.denisolek.cinema.domain.shared.AuthenticationError

interface AuthenticationRepository {
    fun find(token: String): Either<AuthenticationError, Authentication>
}