package com.denisolek.cinema.domain.authentication

import arrow.core.Either
import com.denisolek.cinema.domain.authentication.infrastructure.AuthenticationRepository
import com.denisolek.cinema.domain.shared.AuthenticationError
import com.denisolek.cinema.domain.shared.AuthenticationError.Unauthorized

class AuthenticationFacade(private val repository: AuthenticationRepository) {
    fun authenticate(token: String): Either<AuthenticationError, Authentication> = repository.find(token).mapLeft { Unauthorized() }
}