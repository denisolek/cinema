package com.denisolek.cinema.domain.authentication

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.denisolek.cinema.domain.authentication.Role.APPLICATION
import com.denisolek.cinema.domain.shared.AuthenticationError
import com.denisolek.cinema.domain.shared.AuthenticationError.Forbidden
import com.denisolek.cinema.domain.shared.UserId

data class Authentication(
    val id: UserId,
    val roles: Set<Role>
) {
    fun sufficientFor(vararg requiredRoles: Role): Either<AuthenticationError, Unit> = when {
        missingRequiredRoles(requiredRoles.toSet()) && isNotApplication() -> Forbidden().left()
        else -> Unit.right()
    }

    private fun missingRequiredRoles(requiredRoles: Set<Role>) = !roles.any { it in requiredRoles }

    private fun isNotApplication() = !roles.contains(APPLICATION)

    companion object {
        val applicationAuth = Authentication(UserId("000000"), setOf(APPLICATION))
    }
}