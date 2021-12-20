package com.denisolek.cinema.infrastructure.persistance

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.denisolek.cinema.domain.authentication.Authentication
import com.denisolek.cinema.domain.authentication.Role.*
import com.denisolek.cinema.domain.authentication.infrastructure.AuthenticationRepository
import com.denisolek.cinema.domain.shared.AuthenticationError
import com.denisolek.cinema.domain.shared.UserId
import mu.KotlinLogging.logger

class InMemoryAuthenticationRepository : AuthenticationRepository {
    private val log = logger {}

    private val authentications: MutableMap<String, Authentication> = mutableMapOf(
        "owner-token" to Authentication(UserId("owner-id"), setOf(OWNER, MOVIEGOER)),
        "moviegoer-token" to Authentication(UserId("moviegoer-id"), setOf(MOVIEGOER)),
        "system-token" to Authentication(UserId("system-id"), setOf(APPLICATION))
    )

    override fun find(token: String): Either<AuthenticationError, Authentication> {
        return authentications[token]?.right() ?: AuthenticationError.Unauthorized().left().also {
            log.warn { "Token $token not authenticated" }
        }
    }
}