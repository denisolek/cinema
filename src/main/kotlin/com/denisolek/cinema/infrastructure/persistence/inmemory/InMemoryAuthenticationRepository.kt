package com.denisolek.cinema.infrastructure.persistence.inmemory

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.denisolek.cinema.application.config.DefaultUser
import com.denisolek.cinema.domain.authentication.Authentication
import com.denisolek.cinema.domain.authentication.Role.valueOf
import com.denisolek.cinema.domain.authentication.infrastructure.AuthenticationRepository
import com.denisolek.cinema.domain.shared.IOError
import com.denisolek.cinema.domain.shared.IOError.NotFound
import com.denisolek.cinema.domain.shared.UserId

class InMemoryAuthenticationRepository(defaultUsers: List<DefaultUser>) : AuthenticationRepository {

    private val authentications: Map<String, Authentication> = defaultUsers.associateBy(
        { it.token },
        { Authentication(UserId(it.userId), it.roles.map { role -> valueOf(role) }.toSet()) }
    )

    override fun find(token: String): Either<IOError, Authentication> =
        authentications[token]?.right() ?: NotFound(token).left()
}