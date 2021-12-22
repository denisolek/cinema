package com.denisolek.cinema.domain.authentication.infrastructure

import arrow.core.Either
import com.denisolek.cinema.domain.authentication.Authentication
import com.denisolek.cinema.domain.shared.IOError

interface AuthenticationRepository {
    fun find(token: String): Either<IOError, Authentication>
}