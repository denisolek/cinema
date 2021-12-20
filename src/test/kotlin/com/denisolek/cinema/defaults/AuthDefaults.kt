package com.denisolek.cinema.defaults

import com.denisolek.cinema.domain.authentication.Authentication
import com.denisolek.cinema.domain.authentication.Role.MOVIEGOER
import com.denisolek.cinema.domain.authentication.Role.OWNER
import com.denisolek.cinema.domain.shared.UserId

object AuthDefaults {
    val ownerToken = "owner-token"
    val ownerId = UserId("owner-id")
    val owner = Authentication(ownerId, setOf(OWNER))
    val moviegoerToken = "moviegoer-token"
    val moviegoerId = UserId("moviegoer-id")
    val moviegoer = Authentication(moviegoerId, setOf(MOVIEGOER))
}