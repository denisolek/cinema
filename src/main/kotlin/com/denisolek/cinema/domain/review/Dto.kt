package com.denisolek.cinema.domain.review

import com.denisolek.cinema.domain.authentication.Authentication
import com.denisolek.cinema.domain.shared.MovieId

data class AddReview(
    val authentication: Authentication,
    val movieId: MovieId,
    val stars: Int
)